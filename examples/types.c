typedef struct OtherClass OtherClass;

int IntArrayField[4];

const int GlobalVariable = 10;

enum MyEnum { AA, BB, CC };
enum MyEnum MyEnumField = AA;

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

struct PackedStruct
{
	char a;
	char b;
	char d;
} __attribute__((packed));
struct PackedStruct AAA;

struct MixedData
{
    char Data1;
    short Data2;
    int Data3;
    char Data4;
} BBB;

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

struct Empty
{
} Empty;

typedef struct MyClassExample MyClassExample;
struct MyClassExample
{
	MyClassExample* pointerToSelf;
	struct SuperClass SuperClass;
	
	// Given using preferred names...
	const char a;
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
		
	long double o; // Frankly, this is a complete mess

	//_Quad zz;
	//__float128 p;
	__fp16 p;
	short pp;
	
	// https://gcc.gnu.org/onlinedocs/gcc/Decimal-Float.html#Decimal-Float
	//_Decimal32 d32;
	//_Decimal64 d64;
	//_Decimal128 d128;
	
	//unsigned __int128 r;
	
	_Bool boolean;
	MyClassExample* y;
	OtherClass* z;
	enum MyEnum zz;
	
	int zzz[0][5][10];
};

const MyClassExample * x;

void method(MyClassExample* this)
{
	int x = this->SuperClass.a;
}

void (*functionPointer) (int a, double b);
