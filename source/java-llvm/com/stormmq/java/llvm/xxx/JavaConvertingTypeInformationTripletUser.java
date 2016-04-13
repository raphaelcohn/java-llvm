// The MIT License (MIT)
//
// Copyright © 2016, Raphael Cohn <raphael.cohn@stormmq.com>
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.

package com.stormmq.java.llvm.xxx;

import com.stormmq.java.classfile.processing.Records;
import com.stormmq.java.classfile.processing.TypeInformationTripletUser;
import com.stormmq.java.classfile.processing.typeInformationUsers.TypeInformationTriplet;
import com.stormmq.java.parsing.utilities.names.PackageName;
import com.stormmq.llvm.domain.metadata.creation.*;
import com.stormmq.llvm.domain.target.DataLayoutSpecification;
import org.jetbrains.annotations.NotNull;

import static com.stormmq.llvm.domain.metadata.debugging.LlvmDebugLanguage.DW_LANG_Java;

public final class JavaConvertingTypeInformationTripletUser implements TypeInformationTripletUser
{
	@NotNull private final ModuleCreatorAndWriter moduleCreatorAndWriter;
	@NotNull private final NamespaceSplitter<PackageName> namespaceSplitter;
	@NotNull private final DataLayoutSpecification dataLayoutSpecification;
	@NotNull private final CTypeMappings cTypeMappings;

	public JavaConvertingTypeInformationTripletUser(@NotNull final ModuleCreatorAndWriter moduleCreatorAndWriter, @NotNull final NamespaceSplitter<PackageName> namespaceSplitter, @NotNull final DataLayoutSpecification dataLayoutSpecification, @NotNull final CTypeMappings cTypeMappings)
	{
		this.moduleCreatorAndWriter = moduleCreatorAndWriter;
		this.namespaceSplitter = namespaceSplitter;
		this.dataLayoutSpecification = dataLayoutSpecification;
		this.cTypeMappings = cTypeMappings;
	}

	@Override
	public void use(@NotNull final Records records, @NotNull final TypeInformationTriplet typeInformationTriplet)
	{
		final MetadataCreator<PackageName> metadataCreator = new MetadataCreator<>(DW_LANG_Java, typeInformationTriplet.relativeFilePath, typeInformationTriplet.relativeRootFolderPath, dataLayoutSpecification, namespaceSplitter, cTypeMappings);
		final Process process = new Process(records, typeInformationTriplet, dataLayoutSpecification, metadataCreator);
		process.process(moduleCreatorAndWriter);
	}
}
