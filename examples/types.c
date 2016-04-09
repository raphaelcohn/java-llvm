typedef struct OtherClass OtherClass;

const int GlobalVariable = 10;

struct SuperClass
{
	int a;
};

struct unnamedExample
{
	int a;
	union
	{
		int b;
		float c;
	};
	int d;
	struct
	{
		int e;
		double f;
	};
	struct
	{
		int g;
		double h;
	};
};

struct unnamedExample SomeField;

struct AlignmentExperiment
{
	char a;
	char b;
	__int128 c;
	char d;
} __attribute__((packed));
struct AlignmentExperiment AAA;

struct SizeIs12Bytes
{
  char c1;
  int i;
  char c2;
} XXX;

struct SizeIs8Bytes
{
  int i;
  char c1;
  char c2;
} YYY;

#include <stdint.h>
#include <stdbool.h>
typedef struct MyClassExample MyClassExample;
struct MyClassExample
{
	struct SuperClass SuperClass;
	
	// Given using preferred names...
	char a;
	signed char b;
	unsigned char c;
	short d;
	unsigned short e;
	int f;
	unsigned int g;
	long int h;
	unsigned long int i;
	float j;
	double k;
	
	long long int l;
	long long unsigned int m;
	
	// Creates a typedef that references long long unsigned int
	uint64_t n;
	
	long double o; // Frankly, this is a complete mess

	//_Quad zz;
	//__float128 p;
	
	__int128 q;
	unsigned __int128 r;
	
	bool boolean;
	MyClassExample* y;
	OtherClass* z;
};

const MyClassExample * x;

void method(MyClassExample* this)
{
	int x = this->SuperClass.a;
}

void (*functionPointer) (int a, double b);
