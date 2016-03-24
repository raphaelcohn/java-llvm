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

package com.stormmq.llvm.domain.module;

import com.stormmq.byteWriters.ByteWriter;
import com.stormmq.llvm.domain.Writable;
import com.stormmq.llvm.domain.comdat.ComdatDefinition;
import com.stormmq.llvm.domain.function.FunctionDeclaration;
import com.stormmq.llvm.domain.function.FunctionDefinition;
import com.stormmq.llvm.domain.identifiers.LocalIdentifier;
import com.stormmq.llvm.domain.target.dataLayout.DataLayoutSpecification;
import com.stormmq.llvm.domain.target.triple.TargetTriple;
import com.stormmq.llvm.domain.types.firstClassTypes.aggregateTypes.OpaqueStructureType;
import com.stormmq.llvm.domain.types.firstClassTypes.aggregateTypes.StructureType;
import com.stormmq.llvm.domain.variables.Alias;
import com.stormmq.llvm.domain.variables.GlobalVariable;
import com.stormmq.llvm.metadata.debugging.LlvmDbgCuNamedMetadataTuple;
import com.stormmq.llvm.metadata.module.LlvmIdentNamedMetadataTuple;
import com.stormmq.llvm.metadata.module.LlvmModuleFlagsNamedMetadataTuple;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static com.stormmq.string.StringUtilities.encodeUtf8BytesWithCertaintyValueIsValid;

public final class Module implements Writable
{
	@SuppressWarnings("HardcodedLineSeparator") private static final byte LineFeed = '\n';
	@SuppressWarnings("HardcodedLineSeparator") private static final byte[] SpaceEqualsSpaceTypeSpace = encodeUtf8BytesWithCertaintyValueIsValid(" = type ");

	@NotNull private final DataLayoutSpecification dataLayoutSpecification;
	@NotNull private final TargetTriple targetTriple;
	@NotNull private final LlvmIdentNamedMetadataTuple llvmIdentNamedMetadataTuple;
	@NotNull private final LlvmModuleFlagsNamedMetadataTuple llvmModuleFlagsNamedMetadataTuple;
	@NotNull private final LlvmDbgCuNamedMetadataTuple compileUnits;
	@NotNull private final List<ComdatDefinition> comdatDefinitions;
	@NotNull private final Map<LocalIdentifier, StructureType> structureTypes;
	@NotNull private final Map<LocalIdentifier, OpaqueStructureType> opaqueStructureTypes;
	@NotNull private final List<GlobalVariable<?>> globalVariablesAndConstants;
	@NotNull private final List<FunctionDeclaration> functionDeclarations;
	@NotNull private final List<FunctionDefinition> functionDefinitions;
	@NotNull private final List<Alias> aliases;

	public Module(@NotNull final DataLayoutSpecification dataLayoutSpecification, @NotNull final TargetTriple targetTriple, @NotNull final LlvmIdentNamedMetadataTuple llvmIdentNamedMetadataTuple, @NotNull final LlvmModuleFlagsNamedMetadataTuple llvmModuleFlagsNamedMetadataTuple, @NotNull final LlvmDbgCuNamedMetadataTuple compileUnits, @NotNull final List<ComdatDefinition> comdatDefinitions, @NotNull final Map<LocalIdentifier, StructureType> structureTypes, @NotNull final Map<LocalIdentifier, OpaqueStructureType> opaqueStructureTypes, @NotNull final List<GlobalVariable<?>> globalVariablesAndConstants, @NotNull final List<FunctionDeclaration> functionDeclarations, @NotNull final List<FunctionDefinition> functionDefinitions, @NotNull final List<Alias> aliases)
	{
		this.dataLayoutSpecification = dataLayoutSpecification;
		this.targetTriple = targetTriple;
		this.llvmIdentNamedMetadataTuple = llvmIdentNamedMetadataTuple;
		this.llvmModuleFlagsNamedMetadataTuple = llvmModuleFlagsNamedMetadataTuple;
		this.compileUnits = compileUnits;
		this.comdatDefinitions = comdatDefinitions;
		this.structureTypes = structureTypes;
		this.opaqueStructureTypes = opaqueStructureTypes;
		this.globalVariablesAndConstants = globalVariablesAndConstants;
		this.functionDeclarations = functionDeclarations;
		this.functionDefinitions = functionDefinitions;
		this.aliases = aliases;
	}

	@Override
	public <X extends Exception> void write(@NotNull final ByteWriter<X> byteWriter) throws X
	{
		dataLayoutSpecification.write(byteWriter);
		targetTriple.write(byteWriter);
		Writable.writeLineFeed(byteWriter);

		llvmIdentNamedMetadataTuple.write(byteWriter);

		llvmModuleFlagsNamedMetadataTuple.write(byteWriter);

		compileUnits.write(byteWriter);

		Writable.writeLineFeed(byteWriter);

		write(byteWriter, comdatDefinitions);

		write(byteWriter, globalVariablesAndConstants);

		write(byteWriter, functionDeclarations);

		write(byteWriter, aliases);

		write(byteWriter, structureTypes);

		write(byteWriter, opaqueStructureTypes);

		write(byteWriter, functionDefinitions);
	}

	private static <X extends Exception> void write(@NotNull final ByteWriter<X> byteWriter, @NotNull final List<? extends Writable> writables) throws X
	{
		for (final Writable writable : writables)
		{
			writable.write(byteWriter);
			writeLineFeed(byteWriter);
		}
		writeLineFeed(byteWriter);
	}

	private static <X extends Exception> void write(@NotNull final ByteWriter<X> byteWriter, @NotNull final Map<LocalIdentifier, ? extends Writable> types) throws X
	{
		for (final Entry<LocalIdentifier, ? extends Writable> entry : types.entrySet())
		{
			entry.getKey().write(byteWriter);
			byteWriter.writeBytes(SpaceEqualsSpaceTypeSpace);
			entry.getValue().write(byteWriter);
			writeLineFeed(byteWriter);
			writeLineFeed(byteWriter);
		}
		writeLineFeed(byteWriter);
	}

	private static <X extends Exception> void writeLineFeed(@NotNull final ByteWriter<X> byteWriter) throws X
	{
		byteWriter.writeByte(LineFeed);
	}
}
