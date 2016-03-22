// This file is part of java-llvm. It is subject to the licence terms in the COPYRIGHT file found in the top-level directory of this distribution and at https://raw.githubusercontent.com/raphaelcohn/java-llvm/master/COPYRIGHT. No part of java-llvm, including this file, may be copied, modified, propagated, or distributed except according to the terms contained in the COPYRIGHT file.
// Copyright © 2016 The developers of java-llvm. See the COPYRIGHT file in the top-level directory of this distribution and at https://raw.githubusercontent.com/raphaelcohn/java-llvm/master/COPYRIGHT.
#include <stdio.h>
#include <stdint.h>

const int XXXXXX = 10;

int main()
{
	puts("hello world\n");
	return 5;
}

struct MyClassExample
{
	size_t length;
};

signed int example(struct MyClassExample* ref, _Bool aa, signed char bb, unsigned char cc, signed short ee, unsigned short ff, signed int gg, unsigned int hh, int64_t ii, uint64_t jj, float kk, double ll)
{
	return gg + gg;
}

_Bool example2(_Bool left, _Bool right)
{
	_Bool result = left || right;
	return result;
}
