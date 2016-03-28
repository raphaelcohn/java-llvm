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

package com.stormmq.llvm.metadata.debugging;

public enum DwarfTag
{
	@SuppressWarnings("unused")DW_TAG_base_type,
	@SuppressWarnings("unused")DW_TAG_unspecified_type,

	@SuppressWarnings("unused")DW_TAG_formal_parameter(5, true, false),
	@SuppressWarnings("unused")DW_TAG_member(13, true, false),
	@SuppressWarnings("unused")DW_TAG_pointer_type(15, true, false),
	@SuppressWarnings("unused")DW_TAG_reference_type(16, true, false),
	@SuppressWarnings("unused")DW_TAG_typedef(22, true, false),
	@SuppressWarnings("unused")DW_TAG_ptr_to_member_type(31, true, false),
	@SuppressWarnings("unused")DW_TAG_const_type(38, true, false),
	@SuppressWarnings("unused")DW_TAG_volatile_type(53, true, false),
	@SuppressWarnings("unused")DW_TAG_restrict_type(55, true, false),

	@SuppressWarnings("unused")DW_TAG_array_type(1, false, true),
	@SuppressWarnings("unused")DW_TAG_class_type(2, false, true),
	@SuppressWarnings("unused")DW_TAG_enumeration_type(4, false, true),
	@SuppressWarnings("unused")DW_TAG_structure_type(19, false, true),
	@SuppressWarnings("unused")DW_TAG_union_type(23, false, true),
	@SuppressWarnings("unused")DW_TAG_subroutine_type(21, false, true),
	@SuppressWarnings("unused")DW_TAG_inheritance(28, false, true),
	;

	private final int value;
	public final boolean validForDerivedType;
	public final boolean validForCompositeType;

	DwarfTag()
	{
		value = -1;
		validForDerivedType = false;
		validForCompositeType = false;
	}

	DwarfTag(final int value, final boolean validForDerivedType, final boolean validForCompositeType)
	{
		this.value = value;
		this.validForDerivedType = validForDerivedType;
		this.validForCompositeType = validForCompositeType;
	}
}
