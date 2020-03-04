/*******************************************************************************
* Copyright (c) 2020 Robert Bosch GmbH
* Author: Constantin Ziesche (constantin.ziesche@bosch.com)
*
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
*******************************************************************************/
using BaSyx.API.Components;
using BaSyx.Models.Connectivity.Descriptors;
using BaSyx.Models.Core.Common;
using BaSyx.Models.Extensions;
using BaSyx.Utils.ResultHandling;
using Newtonsoft.Json;
using NLog;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Security.Cryptography;
using System.Text;

namespace BaSyx.Registry.ReferenceImpl.FileBased
{
    public class FileBasedRegistry : IAssetAdministrationShellRegistry
    {
        private static readonly ILogger logger = LogManager.GetCurrentClassLogger();

        public const string SubmodelFolder = "Submodels";
        
        public FileBasedRegistrySettings Settings { get; }
        public JsonSerializerSettings JsonSerializerSettings { get; }
        public string FolderPath { get; }
        public FileBasedRegistry(FileBasedRegistrySettings settings = null)
        {
            Settings = settings ?? FileBasedRegistrySettings.LoadSettings();
            JsonSerializerSettings = new JsonStandardSettings();

            FolderPath = Settings.Miscellaneous["FolderPath"];

            if (string.IsNullOrEmpty(FolderPath))
            {
                logger.Error("FolderPath is null or empty");
                throw new ArgumentNullException("FolderPath");
            }
            if (!Directory.Exists(FolderPath))
            {
                DirectoryInfo info;
                try
                {
                    info = Directory.CreateDirectory(FolderPath);
                }
                catch (Exception e)
                {
                    logger.Error("FolderPath does not exist and cannot be created: " + e.Message);
                    throw;
                }

                if (!info.Exists)
                {
                    logger.Error("FolderPath does not exist and cannot be created");
                    throw new InvalidOperationException("FolderPath does not exist and cannot be created");
                }
            }
        }
        public IResult<IAssetAdministrationShellDescriptor> CreateAssetAdministrationShell(IAssetAdministrationShellDescriptor aasDescriptor)
        {
            if (aasDescriptor == null)
                return new Result<IAssetAdministrationShellDescriptor>(new ArgumentNullException(nameof(aasDescriptor)));
            if (aasDescriptor.Identification?.Id == null)
                return new Result<IAssetAdministrationShellDescriptor>(new ArgumentNullException("aas.Identification"));
            if (string.IsNullOrEmpty(aasDescriptor.IdShort))
                return new Result<IAssetAdministrationShellDescriptor>(new ArgumentNullException("aas.IdShort"));

            try
            {
                string aasIdHash = GetHashString(aasDescriptor.Identification.Id);
                string aasDirectoryPath = Path.Combine(FolderPath, aasIdHash);
                
                if (!Directory.Exists(aasDirectoryPath))
                    Directory.CreateDirectory(aasDirectoryPath);

                if(aasDescriptor.SubmodelDescriptors?.Count > 0)
                {
                    foreach (var submodelDescriptor in aasDescriptor.SubmodelDescriptors)
                    {
                        var interimResult = CreateSubmodel(aasDescriptor.Identification.Id, submodelDescriptor);
                        if (!interimResult.Success)
                            return new Result<IAssetAdministrationShellDescriptor>(interimResult);
                    }
                }
                aasDescriptor.SubmodelDescriptors.Clear();

                string aasDescriptorContent = JsonConvert.SerializeObject(aasDescriptor, JsonSerializerSettings);
                string aasFilePath = Path.Combine(aasDirectoryPath, aasIdHash) + ".json";
                File.WriteAllText(aasFilePath, aasDescriptorContent);

                IResult<IAssetAdministrationShellDescriptor> readResult = RetrieveAssetAdministrationShell(aasDescriptor.Identification.Id);
                return readResult;
            }
            catch (Exception e)
            {
                return new Result<IAssetAdministrationShellDescriptor>(e);
            }
        }

        public IResult<ISubmodelDescriptor> CreateSubmodel(string aasId, ISubmodelDescriptor submodel)
        {
            if (string.IsNullOrEmpty(aasId))
                return new Result<ISubmodelDescriptor>(new ArgumentNullException(nameof(aasId)));
            if (submodel == null || string.IsNullOrEmpty(submodel.IdShort))
                return new Result<ISubmodelDescriptor>(new ArgumentNullException("submodel.IdShort"));

            string aasIdHash = GetHashString(aasId);
            string aasDirectoryPath = Path.Combine(FolderPath, aasIdHash);
            if (!Directory.Exists(aasDirectoryPath))
                return new Result<ISubmodelDescriptor>(false, new Message(MessageType.Error, "AssetAdministrationShell does not exist - register AAS first"));

            try
            {
                string submodelDirectory = Path.Combine(aasDirectoryPath, SubmodelFolder);
                string submodelContent = JsonConvert.SerializeObject(submodel, JsonSerializerSettings);
                if (!Directory.Exists(submodelDirectory))
                    Directory.CreateDirectory(submodelDirectory);

                string submodelFilePath = Path.Combine(submodelDirectory, submodel.IdShort) + ".json";
                File.WriteAllText(submodelFilePath, submodelContent);

                IResult<ISubmodelDescriptor> readSubmodel = RetrieveSubmodel(aasId, submodel.IdShort);
                return readSubmodel;
            }
            catch (Exception e)
            {
                return new Result<ISubmodelDescriptor>(e);
            }
        }

        public IResult DeleteAssetAdministrationShell(string aasId)
        {
            if (string.IsNullOrEmpty(aasId))
                return new Result(new ArgumentNullException(nameof(aasId)));

            string aasIdHash = GetHashString(aasId);
            string aasDirectoryPath = Path.Combine(FolderPath, aasIdHash);
            if (!Directory.Exists(aasDirectoryPath))
                return new Result(true, new Message(MessageType.Information, "No Asset Administration Shell found"));
            else
            {
                try
                {
                    Directory.Delete(aasDirectoryPath, true);
                    return new Result(true);
                }
                catch (Exception e)
                {
                    return new Result(e);
                }
            }
        }

        public IResult DeleteSubmodel(string aasId, string submodelId)
        {
            if (string.IsNullOrEmpty(aasId))
                return new Result(new ArgumentNullException(nameof(aasId)));
            if (string.IsNullOrEmpty(submodelId))
                return new Result(new ArgumentNullException(nameof(submodelId)));

            string aasIdHash = GetHashString(aasId);
            string submodelFilePath = Path.Combine(FolderPath, aasIdHash, SubmodelFolder, submodelId) + ".json";
            if (!File.Exists(submodelFilePath))
                return new Result(true, new Message(MessageType.Information, "No Submodel found"));
            else
            {
                try
                {
                    File.Delete(submodelFilePath);
                    return new Result(true);
                }
                catch (Exception e)
                {
                    return new Result(e);
                }
            }
        }

        public IResult<IAssetAdministrationShellDescriptor> RetrieveAssetAdministrationShell(string aasId)
        {
            if (string.IsNullOrEmpty(aasId))
                return new Result<IAssetAdministrationShellDescriptor>(new ArgumentNullException(nameof(aasId)));

            string aasIdHash = GetHashString(aasId);
            string aasFilePath = Path.Combine(FolderPath, aasIdHash, aasIdHash) + ".json";
            if (File.Exists(aasFilePath))
            {
                try
                {
                    string aasContent = File.ReadAllText(aasFilePath);
                    IAssetAdministrationShellDescriptor descriptor = JsonConvert.DeserializeObject<IAssetAdministrationShellDescriptor>(aasContent, JsonSerializerSettings);

                    var submodelDescriptors = RetrieveSubmodels(aasId);
                    if(submodelDescriptors.Success && submodelDescriptors.Entity?.Count > 0)
                        descriptor.SubmodelDescriptors = submodelDescriptors.Entity;

                    return new Result<IAssetAdministrationShellDescriptor>(true, descriptor);
                }
                catch (Exception e)
                {
                    return new Result<IAssetAdministrationShellDescriptor>(e);
                }
            }
            else
                return new Result<IAssetAdministrationShellDescriptor>(false, new NotFoundMessage("Asset Administration Shell"));

        }

        public IResult<IElementContainer<IAssetAdministrationShellDescriptor>> RetrieveAssetAdministrationShells()
        {
            string[] aasDirectories = Directory.GetDirectories(FolderPath);
            if(aasDirectories == null || aasDirectories.Length == 0)
                return new Result<ElementContainer<IAssetAdministrationShellDescriptor>>(false, new NotFoundMessage("Asset Administration Shells"));
            else
            {
                ElementContainer<IAssetAdministrationShellDescriptor> aasDescriptors = new ElementContainer<IAssetAdministrationShellDescriptor>();
                foreach (var directory in aasDirectories)
                {
                    string aasIdHash = directory.Split(Path.DirectorySeparatorChar).Last();
                    string aasFilePath = Path.Combine(directory, aasIdHash) + ".json";
                    IResult<IAssetAdministrationShellDescriptor> readAASDescriptor = ReadAssetAdministrationShell(aasFilePath);
                    if (readAASDescriptor.Success && readAASDescriptor.Entity != null)
                        aasDescriptors.Create(readAASDescriptor.Entity);
                }
                if (aasDescriptors.Count == 0)
                    return new Result<ElementContainer<IAssetAdministrationShellDescriptor>>(false, new NotFoundMessage("Asset Administration Shells"));
                else
                    return new Result<ElementContainer<IAssetAdministrationShellDescriptor>>(true, aasDescriptors);
            }

        }

        private IResult<IAssetAdministrationShellDescriptor> ReadAssetAdministrationShell(string aasFilePath)
        {
            if (string.IsNullOrEmpty(aasFilePath))
                return new Result<IAssetAdministrationShellDescriptor>(new ArgumentNullException(nameof(aasFilePath)));

            if (File.Exists(aasFilePath))
            {
                try
                {
                    string aasContent = File.ReadAllText(aasFilePath);
                    IAssetAdministrationShellDescriptor descriptor = JsonConvert.DeserializeObject<IAssetAdministrationShellDescriptor>(aasContent, JsonSerializerSettings);

                    var submodelDescriptors = RetrieveSubmodels(descriptor.Identification.Id);
                    if (submodelDescriptors.Success && submodelDescriptors.Entity != null)
                        descriptor.SubmodelDescriptors = submodelDescriptors.Entity;

                    return new Result<IAssetAdministrationShellDescriptor>(true, descriptor);
                }
                catch (Exception e)
                {
                    return new Result<IAssetAdministrationShellDescriptor>(e);
                }
            }
            else
                return new Result<IAssetAdministrationShellDescriptor>(false, new NotFoundMessage("Asset Administration Shell"));

        }

        public IResult<ISubmodelDescriptor> RetrieveSubmodel(string aasId, string submodelId)
        {
            if (string.IsNullOrEmpty(aasId))
                return new Result<ISubmodelDescriptor>(new ArgumentNullException(nameof(aasId)));
            if (string.IsNullOrEmpty(submodelId))
                return new Result<ISubmodelDescriptor>(new ArgumentNullException(nameof(submodelId)));

            string aasIdHash = GetHashString(aasId);
            string aasDirectoryPath = Path.Combine(FolderPath, aasIdHash);
            if (Directory.Exists(aasDirectoryPath))
            {
                string submodelPath = Path.Combine(aasDirectoryPath, SubmodelFolder, submodelId) + ".json";
                if (File.Exists(submodelPath))
                {
                    try
                    {
                        string submodelContent = File.ReadAllText(submodelPath);
                        ISubmodelDescriptor descriptor = JsonConvert.DeserializeObject<ISubmodelDescriptor>(submodelContent, JsonSerializerSettings);
                        return new Result<ISubmodelDescriptor>(true, descriptor);
                    }
                    catch (Exception e)
                    {
                        return new Result<ISubmodelDescriptor>(e);
                    }
                }
                else
                    return new Result<ISubmodelDescriptor>(false, new NotFoundMessage("Submodel"));
            }
            else
                return new Result<ISubmodelDescriptor>(false, new NotFoundMessage("Asset Administration Shell"));
        }

        public IResult<IElementContainer<ISubmodelDescriptor>> RetrieveSubmodels(string aasId)
        {
            if (string.IsNullOrEmpty(aasId))
                return new Result<ElementContainer<ISubmodelDescriptor>>(new ArgumentNullException(nameof(aasId)));

            string aasIdHash = GetHashString(aasId);
            string aasDirectoryPath = Path.Combine(FolderPath, aasIdHash);
            if (Directory.Exists(aasDirectoryPath))
            {
                string submodelDirectoryPath = Path.Combine(aasDirectoryPath, SubmodelFolder);
                if (Directory.Exists(submodelDirectoryPath))
                {
                    ElementContainer<ISubmodelDescriptor> submodelDescriptors = new ElementContainer<ISubmodelDescriptor>();
                    string[] files = Directory.GetFiles(submodelDirectoryPath);
                    foreach (var file in files)
                    {
                        string submodelId = file.Split(Path.DirectorySeparatorChar)?.Last()?.Replace(".json", "");
                        var descriptor = RetrieveSubmodel(aasId, submodelId);
                        if (descriptor.Success && descriptor.Entity != null)
                            submodelDescriptors.Create(descriptor.Entity);
                    }
                    if (submodelDescriptors.Count == 0)
                        return new Result<ElementContainer<ISubmodelDescriptor>>(false, new NotFoundMessage("Submodels"));
                    else
                        return new Result<ElementContainer<ISubmodelDescriptor>>(true, submodelDescriptors);
                }
                else
                    return new Result<ElementContainer<ISubmodelDescriptor>>(false, new NotFoundMessage("Submodel"));
            }
            else
                return new Result<ElementContainer<ISubmodelDescriptor>>(false, new NotFoundMessage("Asset Administration Shell"));
        }

        public IResult UpdateAssetAdministrationShell(string aasId, Dictionary<string, string> metaData)
        {
            IResult<IAssetAdministrationShellDescriptor> readAAS = ReadAssetAdministrationShell(aasId);
            return new Result(readAAS);
        }

        private static string GetHashString(string input)
        {
            SHA256 shaAlgorithm = SHA256.Create();
            byte[] data = Encoding.UTF8.GetBytes(input);

            byte[] bHash = shaAlgorithm.ComputeHash(data);

            string hashString = string.Empty;
            for (int i = 0; i < bHash.Length; i++)
            {
                hashString += bHash[i].ToString("x2");

                //break condition for filename length
                if (i == 255)
                    break;
            }
            return hashString;
        }
    }
}
