﻿/*******************************************************************************
* Copyright (c) 2020 Robert Bosch GmbH
* Author: Constantin Ziesche (constantin.ziesche@bosch.com)
*
* This program and the accompanying materials are made available under the
* terms of the Eclipse Distribution License 1.0 which is available at
* https://www.eclipse.org/org/documents/edl-v10.html
*
* 
*******************************************************************************/
using BaSyx.Models.Core.AssetAdministrationShell;
using BaSyx.Models.Core.AssetAdministrationShell.Identification;
using BaSyx.Models.Core.AssetAdministrationShell.Implementations;
using BaSyx.Models.Core.Common;
using BaSyx.Models.Extensions;
using BaSyx.Utils.ResultHandling;
using System;
using System.Threading.Tasks;

namespace BaSyx.Components.Tests
{
    public static class TestAssetAdministrationShell
    {
        public static AssetAdministrationShell GetAssetAdministrationShell()
        {
            AssetAdministrationShell aas = new AssetAdministrationShell("TestAAS", new Identifier("http://basys40.de/shells/SimpleAAS/" + Guid.NewGuid().ToString(), KeyType.IRI))
            {
                Description = new LangStringSet()
                {
                   new LangString("de-DE", "Einfache VWS"),
                   new LangString("en-US", "Simple AAS")
                },
                Administration = new AdministrativeInformation()
                {
                    Version = "1.0",
                    Revision = "120"
                },
                Asset = new Asset("TestAsset", new Identifier("http://basys40.de/assets/SimpleAsset/" + Guid.NewGuid().ToString(), KeyType.IRI))
                {
                    Kind = AssetKind.Instance,
                    Description = new LangStringSet()
                    {
                          new LangString("de-DE", "Einfaches Asset"),
                          new LangString("en-US", "Simple Asset")
                    }
                }
            };

            BaSyx.Models.Core.AssetAdministrationShell.Implementations.Submodel testSubmodel = GetTestSubmodel();

            aas.Submodels.Add(testSubmodel);

            return aas;
        }

        public static BaSyx.Models.Core.AssetAdministrationShell.Implementations.Submodel GetTestSubmodel()
        {
            string propertyValue = "TestFromInside";
            int i = 0;
            double y = 2.0;

            BaSyx.Models.Core.AssetAdministrationShell.Implementations.Submodel testSubmodel = new BaSyx.Models.Core.AssetAdministrationShell.Implementations.Submodel("TestSubmodel", new Identifier(Guid.NewGuid().ToString(), KeyType.Custom))
            {
                SubmodelElements =
                {
                    new Property<string>("TestProperty1")
                    {
                        Set = (prop, val) => propertyValue = val,
                        Get = prop => { return propertyValue + "_" + i++; }
                    },
                    new Property<string>("TestProperty2")
                    {
                        Set = (prop, val) => propertyValue = val,
                        Get = prop => { return propertyValue + "_" + i++; }
                    },
                    new Property<int>("TestProperty3")
                    {
                        Set = (prop, val) => i = val,
                        Get = prop => { return i++; }
                    },
                    new Property<double>("TestProperty4")
                    {
                        Set = (prop, val) => y = val,
                        Get = prop => { return Math.Pow(y, i); }
                    },
                    new SubmodelElementCollection("TestSubmodelElementCollection")
                    {
                        Value =
                        {
                            new Property<string>("TestSubProperty1")
                            {
                                Set = (prop, val) => propertyValue = val,
                                Get = prop => { return propertyValue + "_" + i--; }
                            },
                            new Property<string>("TestSubProperty2")
                            {
                                Set = (prop, val) => propertyValue = val,
                                Get = prop => { return propertyValue + "_" + i--; }
                            },
                            new Property<int>("TestSubProperty3")
                            {
                                Set = (prop, val) => i = val,
                                Get = prop => { return i--; }
                            },
                            new Property<double>("TestSubProperty4")
                            {
                                Set = (prop, val) => y = val,
                                Get = prop => { return Math.Pow(y, i); }
                            }
                        }
                    },
                    new Operation("GetTime")
                    {
                        OutputVariables = new OperationVariableSet()
                        {
                            new Property<string>("Date"),
                            new Property<string>("Time"),
                            new Property<string>("Ticks")
                        },
                        OnMethodCalled = (op, inArgs, inOutArgs, outArgs, cancellationToken) =>
                        {
                            outArgs.Add(new Property<string>("Date") { Value = "Heute ist der " + DateTime.Now.Date.ToString() });
                            outArgs.Add(new Property<string>("Time") { Value = "Es ist " + DateTime.Now.TimeOfDay.ToString() + " Uhr" });
                            outArgs.Add(new Property<string>("Ticks") { Value = "Ticks: " + DateTime.Now.Ticks.ToString() });
                            return new OperationResult(true);
                        }
                    },
                    new Operation("Calculate")
                    {
                        Description = new LangStringSet()
                        {
                            new LangString("DE", "Taschenrechner mit simulierter langer Rechenzeit zum Testen von asynchronen Aufrufen"),
                            new LangString("EN", "Calculator with simulated long-running computing time for testing asynchronous calls")
                        },
                        InputVariables = new OperationVariableSet()
                        {
                            new Property<string>("Expression")
                            {
                                Description = new LangStringSet()
                                {
                                    new LangString("DE", "Ein mathematischer Ausdruck (z.B. 5*9)"),
                                    new LangString("EN", "A mathematical expression (e.g. 5*9)")
                                }
                            },
                            new Property<int>("ComputingTime")
                            {
                                Description = new LangStringSet()
                                {
                                    new LangString("DE", "Die Bearbeitungszeit in Millisekunden"),
                                    new LangString("EN", "The computation time in milliseconds")
                                }
                            }
                        },
                       OutputVariables = new OperationVariableSet()
                       {
                           new Property<double>("Result")
                       },
                       OnMethodCalled = async (op, inArgs, inOutArgs, outArgs, cancellationToken) =>
                       {
                           string expression = inArgs["Expression"]?.GetValue<string>();
                           int? computingTime = inArgs["ComputingTime"]?.GetValue<int>();

                           string throughput = inOutArgs["ThroughputVariable"]?.GetValue<string>();
                           if(!string.IsNullOrEmpty(throughput))
                            inOutArgs["ThroughputVariable"].SetValue(throughput + " modified in Calculate Method");
                           

                           if(computingTime.HasValue)
                            await Task.Delay(computingTime.Value, cancellationToken);
                     
                           if(cancellationToken.IsCancellationRequested)
                               return new OperationResult(false, new Message(MessageType.Information, "Cancellation was requested"));

                           double value = CalulcateExpression(expression);

                           outArgs.Add(new Property<double>("Result", value));
                           return new OperationResult(true);
                       }
                    }

                }
            };
            return testSubmodel;
        }

        public static double CalulcateExpression(string expression)
        {
            string columnName = "Evaluation";
            System.Data.DataTable dataTable = new System.Data.DataTable();
            System.Data.DataColumn dataColumn = new System.Data.DataColumn(columnName, typeof(double), expression);
            dataTable.Columns.Add(dataColumn);
            dataTable.Rows.Add(0);
            return (double)(dataTable.Rows[0][columnName]);
        }
    }
}