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

package com.stormmq.llvm.domain.metadata.creation;

import com.stormmq.functions.TriFunction;
import com.stormmq.llvm.domain.metadata.debugging.DwarfTypeEncoding;
import com.stormmq.llvm.domain.target.*;
import com.stormmq.llvm.domain.types.SizedType;
import com.stormmq.llvm.domain.types.firstClassTypes.FloatingPointValueType;
import com.stormmq.llvm.domain.types.firstClassTypes.IntegerValueType;
import com.stormmq.string.AbstractToString;
import com.stormmq.string.StringConstants;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static com.stormmq.functions.collections.MapHelper.getGuarded;
import static com.stormmq.functions.collections.MapHelper.putOnce;
import static com.stormmq.llvm.domain.metadata.debugging.DwarfTypeEncoding.*;
import static com.stormmq.llvm.domain.types.firstClassTypes.FloatingPointValueType.*;
import static com.stormmq.llvm.domain.types.firstClassTypes.IntegerValueType.*;
import static com.stormmq.string.StringConstants.*;

public final class CTypeMappings extends AbstractToString
{
	@NotNull @NonNls private static final String _Bool = "_Bool";
	@NotNull @NonNls private static final String signed_char = "signed char";
	@NotNull @NonNls private static final String unsigned_char = "unsigned char";
	@NotNull @NonNls private static final String wchar_t = "wchar_t";
	@NotNull @NonNls private static final String char16_t = "char16_t";
	@NotNull @NonNls private static final String char32_t = "char32_t";
	@NotNull @NonNls private static final String unsigned_short = "unsigned short";
	@NotNull @NonNls private static final String unsigned_int = "unsigned int";
	@NotNull @NonNls private static final String long_int = "long int";
	@NotNull @NonNls private static final String long_unsigned_int = "long unsigned int";
	@NotNull @NonNls private static final String long_long_int = "long long int";
	@NotNull @NonNls private static final String long_long_unsigned_int = "long long unsigned int";
	@NotNull @NonNls private static final String __int128 = "__int128";
	@NotNull @NonNls private static final String unsigned___int128 = "unsigned __int128";
	@NotNull @NonNls private static final String __fp16 = "__fp16";
	@NotNull @NonNls private static final String _float = StringConstants._float;
	@NotNull @NonNls private static final String _double = StringConstants._double;
	@NotNull @NonNls private static final String long_double = "long double";
	@NotNull @NonNls private static final String __float128 = "__float128";

	@NonNls @NotNull private final String cNameThatClosestMatchesASigned32BitInteger;
	@NotNull private final Map<String, CTypeMapping> mappings;

	public CTypeMappings(@NotNull final CDataModel cDataModel)
	{
		if (cDataModel.isCShortSizeNotSixteenBits())
		{
			throw new UnsupportedOperationException("The C data model does not support shorts (signed or unsigned) in a compatible way");
		}

		cNameThatClosestMatchesASigned32BitInteger = cDataModel.determineCNameThatClosestMatchesAnUnsigned16BitInteger(long_int, _int);

		final FloatingPointValueType floatingPointValueType = cDataModel.chooseLongDouble(FloatingPointValueType._double, x86_fp80, ppc_fp128, fp128, half);
		if (floatingPointValueType == half)
		{
			throw new UnsupportedOperationException("MIPS-64 long double type is not supported at this time (and probably not ever); clang uses the newer fp128 to model it");
		}

		final IntegerValueType shortInt = choose(cDataModel::chooseShortSignedAndUnsigned);
		final IntegerValueType normalInt = choose(cDataModel::chooseIntSignedAndUnsigned);
		final IntegerValueType longInt = choose(cDataModel::chooseLongIntSignedAndUnsigned);
		final IntegerValueType wideCharT = choose(cDataModel::chooseWideCharT);

		mappings = new HashMap<>(16);

		map(_Bool, i1, DW_ATE_boolean, 'b');
		map(_char, i8, DW_ATE_signed_char, 'c');
		map(signed_char, i8, DW_ATE_signed_char, 'a');
		map(unsigned_char, i8, DW_ATE_unsigned_char, 'h');
		map(wchar_t, wideCharT, DW_ATE_signed, 'w');
		map(char16_t, i16, DW_ATE_UTF, "Ds");
		map(char32_t, i32, DW_ATE_UTF, "Di");
		map(_short, shortInt, DW_ATE_signed, 's');
		map(unsigned_short, shortInt, DW_ATE_unsigned, 't');
		map(_int, normalInt, DW_ATE_signed, 'i');
		map(unsigned_int, normalInt, DW_ATE_unsigned, 'j');
		map(long_int, longInt, DW_ATE_signed, 'l');
		map(long_unsigned_int, longInt, DW_ATE_unsigned, 'm');
		map(long_long_int, i64, DW_ATE_signed, 'x');
		map(long_long_unsigned_int, i64, DW_ATE_unsigned, 'y');
		map(__int128, i128, DW_ATE_signed, 'n');
		map(unsigned___int128, i128, DW_ATE_unsigned, 'o');
		map(__fp16, half, DW_ATE_float, "Dh");
		map(_float, FloatingPointValueType._float, DW_ATE_float, 'f');
		map(_double, FloatingPointValueType._double, DW_ATE_float, 'd');
		map(long_double, floatingPointValueType, DW_ATE_float, 'e');
		map(__float128, fp128, DW_ATE_float, 'g');
	}

	@NotNull
	@Override
	protected Object[] fields()
	{
		return fields(cNameThatClosestMatchesASigned32BitInteger, mappings);
	}

	@NotNull
	private static IntegerValueType choose(@NotNull final TriFunction<IntegerValueType, IntegerValueType, IntegerValueType, IntegerValueType> choose)
	{
		return choose.apply(i16, i32, i64);
	}

	private void map(@NotNull @NonNls final String cName, @NotNull final SizedType sizedType, @NotNull final DwarfTypeEncoding dwarfTypeEncoding, final char itaniumAbiCxxManglingCode)
	{
		map(cName, sizedType, dwarfTypeEncoding, String.valueOf(itaniumAbiCxxManglingCode));
	}

	private void map(@NotNull @NonNls final String cName, @NotNull final SizedType sizedType, @NotNull final DwarfTypeEncoding dwarfTypeEncoding, @NotNull @NonNls final String itaniumAbiCxxManglingCode)
	{
		putOnce(mappings, cName, new CTypeMapping(sizedType, dwarfTypeEncoding, itaniumAbiCxxManglingCode));
	}

	@SuppressWarnings("MethodMayBeStatic")
	@NonNls
	@NotNull
	public String cNameThatClosestMatchesABoolean()
	{
		return _Bool;
	}

	@SuppressWarnings("MethodMayBeStatic")
	@NonNls
	@NotNull
	public String cNameThatClosestMatchesASigned8BitInteger()
	{
		return signed_char;
	}

	@SuppressWarnings("MethodMayBeStatic")
	@NonNls
	@NotNull
	public String cNameThatClosestMatchesASigned16BitInteger()
	{
		return _short;
	}

	@SuppressWarnings("MethodMayBeStatic")
	@NonNls
	@NotNull
	public String cNameThatClosestMatchesAnUnsigned16BitInteger()
	{
		return unsigned_short;
	}

	@NonNls
	@NotNull
	public String cNameThatClosestMatchesASigned32BitInteger()
	{
		return cNameThatClosestMatchesASigned32BitInteger;
	}

	@SuppressWarnings("MethodMayBeStatic")
	@NonNls
	@NotNull
	public String cNameThatClosestMatchesASigned64BitInteger()
	{
		return long_long_int;
	}

	@SuppressWarnings("MethodMayBeStatic")
	@NonNls
	@NotNull
	public String cNameThatClosestMatchesABinary32Float()
	{
		return _float;
	}

	@SuppressWarnings("MethodMayBeStatic")
	@NonNls
	@NotNull
	public String cNameThatClosestMatchesABinary64Float()
	{
		return _double;
	}

	@NotNull
	public CTypeMapping basicType(@NotNull @NonNls final String cName)
	{
		return getGuarded(mappings, cName);
	}
}
