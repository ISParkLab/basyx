/*******************************************************************************
* Copyright (c) 2020, 2021 Robert Bosch GmbH
* Author: Constantin Ziesche (constantin.ziesche@bosch.com)
*
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
*******************************************************************************/
using BaSyx.Models.Core.Common;
using Newtonsoft.Json;
using System.Xml.Serialization;

namespace BaSyx.Models.Export
{
    public class Capability_V2_0 : SubmodelElementType_V2_0
    {
        [JsonProperty("modelType")]
        [XmlIgnore]
        public override ModelType ModelType => ModelType.Capability;

        public Capability_V2_0() { }
        public Capability_V2_0(SubmodelElementType_V2_0 submodelElementType) : base(submodelElementType) { }
    }
}
