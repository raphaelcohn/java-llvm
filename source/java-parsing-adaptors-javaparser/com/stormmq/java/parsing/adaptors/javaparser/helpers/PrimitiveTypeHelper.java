package com.stormmq.java.parsing.adaptors.javaparser.helpers;

import com.github.javaparser.ast.type.PrimitiveType;
import com.stormmq.java.parsing.utilities.names.typeNames.PrimitiveTypeName;
import org.jetbrains.annotations.NotNull;

public final class PrimitiveTypeHelper
{
	private PrimitiveTypeHelper()
	{
	}

	@NotNull
	public static PrimitiveTypeName convertToPrimitiveTypeName(@NotNull final PrimitiveType primitiveType)
	{
		switch (primitiveType.getType())
		{
			case Boolean:
				return _boolean;

			case Char:
				return _char;

			case Byte:
				return _byte;

			case Short:
				return _short;

			case Int:
				return _int;

			case Long:
				return _long;

			case Float:
				return _float;

			case Double:
				return _double;

			default:
				throw new IllegalStateException("Unanticipated primitive");
		}
	}
}
