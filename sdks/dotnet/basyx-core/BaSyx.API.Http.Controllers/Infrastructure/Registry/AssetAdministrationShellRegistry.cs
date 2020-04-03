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
using System.Collections.Generic;
using Microsoft.AspNetCore.Mvc;
using BaSyx.API.Components;
using BaSyx.Utils.ResultHandling;
using System.Web;
using BaSyx.Models.Connectivity.Descriptors;
using BaSyx.Models.Core.Common;

namespace BaSyx.API.Http.Controllers
{
    public class AssetAdministrationShellRegistry : Controller, IAssetAdministrationShellRegistry
    {
        private readonly IAssetAdministrationShellRegistry aasRegistryImpl;
        public AssetAdministrationShellRegistry(IAssetAdministrationShellRegistry aasRegistry)
        {
            aasRegistryImpl = aasRegistry;
        }

        #region REST-Interface
        /// <summary>
        /// Retrieves all registered Asset Administration Shells within a defined system (e.g. site, area, production line, station)
        /// </summary>
        /// <returns></returns>
        /// <response code="200">Returns a list of found Asset Administration Shells</response>        
        /// <response code="400">Bad Request</response>    
        /// <response code="502">Bad Gateway</response>
        [HttpGet("api/v1/registry", Name = "GetAssetAdministrationShells")]
        [ProducesResponseType(typeof(IResult<List<IAssetAdministrationShellDescriptor>>), 200)]
        public IActionResult GetAssetAdministrationShells()
        {
            var result = RetrieveAssetAdministrationShells();
            return result.CreateActionResult(CrudOperation.Retrieve);
        }
        /// <summary>
        /// Retrieves a specific Asset Administration Shell
        /// </summary>
        /// <param name="aasId">The Asset Administration Shell's unique id</param>
        /// <returns></returns>
        /// <response code="200">Returns the requested Asset Administration Shell</response>
        /// <response code="400">Bad Request</response> 
        /// <response code="404">No Asset Administration Shell with passed id found</response>     
        /// <response code="502">Bad Gateway</response>
        [HttpGet("api/v1/registry/{*aasId}", Name = "GetAssetAdministrationShell")]
        [ProducesResponseType(typeof(IResult<IAssetAdministrationShellDescriptor>), 200)]
        public IActionResult GetAssetAdministrationShell(string aasId)
        {
            aasId = HttpUtility.UrlDecode(aasId);
            var result = RetrieveAssetAdministrationShell(aasId);
            return result.CreateActionResult(CrudOperation.Retrieve);
        }
        /// <summary>
        /// Renews a specific Asset Administration Shell's registration
        /// </summary>
        /// <param name="aasId">The Asset Administration Shell's unique id</param>
        /// <returns></returns>
        /// <response code="200">The Asset Administration Shell's registration was successfully renewed</response>
        /// <response code="400">The syntax of the passed Asset Administration Shell is not valid or malformed request</response>    
        /// <response code="404">No Asset Administration Shell with passed id found</response>     
        /// <response code="502">Bad Gateway</response>   
        [HttpPut("api/v1/registry/{*aasId}", Name = "PutAssetAdministrationShell")]
        [ProducesResponseType(typeof(IResult), 204)]
        public IActionResult PutAssetAdministrationShell(string aasId)
        {
            aasId = HttpUtility.UrlDecode(aasId);
            Dictionary<string, string> keyValues = null;
            if (Request.Query?.Count > 0)
            {
                keyValues = new Dictionary<string, string>();
                foreach (string key in Request.Query.Keys)
                {
                    keyValues.Add(key, Request.Query[key].ToString());
                }
            }
            var result = UpdateAssetAdministrationShell(aasId, keyValues);
            return result.CreateActionResult(CrudOperation.Update);
        }
        /// <summary>
        /// Registers a new Asset Administration Shell
        /// </summary>
        /// <param name="aas">The Asset Administration Shell descriptor object</param>
        /// <returns></returns>
        /// <response code="201">The Asset Administration Shell was created successfully</response>
        /// <response code="400">The syntax of the passed Asset Administration Shell is not valid or malformed request</response>             
        /// <response code="422">The passed Asset Administration Shell conflicts with already registered Asset Administration Shells</response>
        /// <response code="502">Bad Gateway</response> 
        [HttpPost("api/v1/registry", Name = "PostAssetAdministrationShell")]
        [ProducesResponseType(typeof(IResult<IAssetAdministrationShellDescriptor>), 201)]
        public IActionResult PostAssetAdministrationShell([FromBody] IAssetAdministrationShellDescriptor aas)
        {
            var result = CreateAssetAdministrationShell(aas);
            return result.CreateActionResult(CrudOperation.Create, "api/v1/registry/"+ HttpUtility.UrlEncode(aas.Identification.Id));
        }
        /// <summary>
        /// Deletes a specific Asset Administration Shell
        /// </summary>
        /// <param name="aasId">The Asset Administration Shell's unique id</param>
        /// <returns></returns>
        /// <response code="200">The Asset Administration Shell was deleted successfully</response>
        /// <response code="400">Bad Request</response>  
        /// <response code="404">No Asset Administration Shell with passed id found</response>     
        /// <response code="502">Bad Gateway</response>
        [HttpDelete("api/v1/registry/{*aasId}", Name = "DeleteAssetAdministrationShell_")]
        [ProducesResponseType(typeof(IResult), 204)]
        public IActionResult DeleteAssetAdministrationShell_(string aasId)
        {
            aasId = HttpUtility.UrlDecode(aasId);
            var result = DeleteAssetAdministrationShell(aasId);
            return result.CreateActionResult(CrudOperation.Delete);
        }


        /// <summary>
        /// Adds a new Submodel to an existing resp. registered Asset Administration Shell
        /// </summary>
        /// <param name="aasId">The Asset Administration Shell's unique id</param>
        /// <param name="submodel">The Submodel descriptor object</param>
        /// <returns></returns>
        /// <response code="201">The Submodel was created successfully</response>
        /// <response code="400">The syntax of the passed Submodel is not valid or malformed request</response>      
        /// <response code="404">No Asset Administration Shell with passed id found</response>   
        /// <response code="422">The passed Submodel conflicts with already registered Submodels</response>
        /// <response code="502">Bad Gateway</response>
        [HttpPost("api/v1/registry/{aasId}/submodels", Name = "PostSubmodelToRegistry")]
        [ProducesResponseType(typeof(IResult<ISubmodelDescriptor>), 201)]
        public IActionResult PostSubmodelToRegistry(string aasId, [FromBody] ISubmodelDescriptor submodel)
        {
            aasId = HttpUtility.UrlDecode(aasId);
            var result = CreateSubmodel(aasId, submodel);
            return result.CreateActionResult(CrudOperation.Create, "api/v1/registry/" + aasId + "/submodels/" + submodel.IdShort);
        }

        /// <summary>
        /// Retrieves a specific Submodel from a specific Asset Administration Shell
        /// </summary>
        /// <param name="aasId">The Asset Administration Shell's unique id</param>
        /// <param name="submodelIdShort">The Submodel's short id (idShort)</param>
        /// <returns></returns>
        /// <response code="200">Returns the requested Submodels</response>
        /// <response code="400">Bad Request</response>
        /// <response code="404">No Asset Administration Shell / Submodel with passed id found</response>     
        /// <response code="502">Bad Gateway</response> 
        [HttpGet("api/v1/registry/{aasId}/submodels/{submodelIdShort}", Name = "GetSubmodelFromRegistry")]
        [ProducesResponseType(typeof(IResult<ISubmodelDescriptor>), 200)]
        public IActionResult GetSubmodelFromRegistry(string aasId, string submodelIdShort)
        {
            aasId = HttpUtility.UrlDecode(aasId);
            var result = RetrieveSubmodel(aasId, submodelIdShort);
            return result.CreateActionResult(CrudOperation.Retrieve);
        }
        /// <summary>
        /// Deletes a specific Submodel from a specific Asset Administration Shell
        /// </summary>
        /// <param name="aasId">The Asset Administration Shell's unique id</param>
        /// <param name="submodelIdShort">The Submodel's short id (idShort)</param>
        /// <returns></returns>
        /// <response code="200">The Submodel was deleted successfully</response>
        /// <response code="400">Bad Request</response>    
        /// <response code="404">No Asset Administration Shell / Submodel with passed id found</response>  
        /// <response code="502">Bad Gateway</response>
        [HttpDelete("api/v1/registry/{aasId}/submodels/{submodelIdShort}", Name = "DeleteSubmodelFromRegistry")]
        [ProducesResponseType(typeof(IResult), 204)]
        public IActionResult DeleteSubmodelFromRegistry(string aasId, string submodelIdShort)
        {
            aasId = HttpUtility.UrlDecode(aasId);
            var result = DeleteSubmodel(aasId, submodelIdShort);
            return result.CreateActionResult(CrudOperation.Delete);
        }
        /// <summary>
        /// Retrieves all Submodels from a specific Asset Administration Shell
        /// </summary>
        /// <param name="aasId">The Asset Administration Shell's unique id</param>
        /// <returns></returns>
        /// <response code="200">Returns a list of found Submodels</response>
        /// <response code="400">Bad Request</response>  
        /// <response code="404">No Asset Administration Shell with passed id found</response>  
        /// <response code="502">Bad Gateway</response>   
        [HttpGet("api/v1/registry/{aasId}/submodels", Name = "GetSubmodelsFromRegistry")]
        [ProducesResponseType(typeof(IResult<List<ISubmodelDescriptor>>), 200)]
        public IActionResult GetSubmodelsFromRegistry(string aasId)
        {
            aasId = HttpUtility.UrlDecode(aasId);
            var result = RetrieveSubmodels(aasId);
            return result.CreateActionResult(CrudOperation.Retrieve);
        }
        #endregion

        #region InterfaceImplementation
        public IResult<IAssetAdministrationShellDescriptor> CreateAssetAdministrationShell(IAssetAdministrationShellDescriptor aas)
        {
            return aasRegistryImpl.CreateAssetAdministrationShell(aas);
        }

        public IResult DeleteAssetAdministrationShell(string aasId)
        {
            return aasRegistryImpl.DeleteAssetAdministrationShell(aasId);
        }

        public IResult<IAssetAdministrationShellDescriptor> RetrieveAssetAdministrationShell(string aasId)
        {
            return aasRegistryImpl.RetrieveAssetAdministrationShell(aasId);
        }

        public IResult<IElementContainer<IAssetAdministrationShellDescriptor>> RetrieveAssetAdministrationShells()
        {
            return aasRegistryImpl.RetrieveAssetAdministrationShells();
        }

        public IResult UpdateAssetAdministrationShell(string aasId, Dictionary<string, string> metaData)
        {
            return aasRegistryImpl.UpdateAssetAdministrationShell(aasId, metaData);
        }

        public IResult<ISubmodelDescriptor> CreateSubmodel(string aasId, ISubmodelDescriptor submodel)
        {
            return aasRegistryImpl.CreateSubmodel(aasId, submodel);
        }

        public IResult<IElementContainer<ISubmodelDescriptor>> RetrieveSubmodels(string aasId)
        {
            return aasRegistryImpl.RetrieveSubmodels(aasId);
        }

        public IResult<ISubmodelDescriptor> RetrieveSubmodel(string aasId, string submodelIdShort)
        {
            return aasRegistryImpl.RetrieveSubmodel(aasId, submodelIdShort);
        }

        public IResult DeleteSubmodel(string aasId, string submodelIdShort)
        {
            return aasRegistryImpl.DeleteSubmodel(aasId, submodelIdShort);
        }


        #endregion

        #region Helper Methods


        #endregion
    }
}
