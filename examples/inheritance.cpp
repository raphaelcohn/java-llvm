/*
	https://en.wikipedia.org/wiki/Name_mangling#Real-world_effects_of_C.2B.2B_name_mangling

	IA-64, GCC 3+, clang, Intel; everything bar Windows/Digital Mars
	
	Function int com.stormmq.inheritance.Depositor.hold(int)
	Mangled name starts with  _Z										_Z
	Then  N  (if in a namespace)										_ZN
	Then  length(identifier)identifier for each part of the namespace	_ZN3com7stormmq11inheritance
	Then  length(identifier)identifier for the class name				_ZN3com7stormmq11inheritance9Depositor
	Then  length(identifier)identifier for the function name			_ZN3com7stormmq11inheritance9Depositor4hold
	Then  E  (if in a namespace)										_ZN3com7stormmq11inheritance9Depositor4holdE
	Then  parameter type information									_ZN3com7stormmq11inheritance9Depositor4holdEi
			v for void if there are no parameters
			i for int
	
	Function dosomething()
	Mangled name starts with  _Z										_Z
	No namespace														_Z
	No class name														_Z
	Then  length(identifier)identifier for the function name			_Z11dosomething
	No E (no namespace)													_Z11dosomething
	
	
	
	
	_Z11dosomethingv
*/

#include <cstddef>

void dosomething(void*);

namespace com
{
	void doX(void* ref);
	
	namespace stormmq
	{
		namespace inheritance
		{
			// Interface
			class HasAnnotation
			{
				public:
					virtual int hasAnnotation();
					
					virtual int doesNotHaveAnnotation();
			};
			
			// Interface
			class Depositor
			{
				public:
					virtual void deposit(double amount);
				
					static int increment(int amount)
					{
						int a = 1;
						long unsigned int x = 5;
						int * aa = &a;
						return amount + hold(NULL, 1, NULL, 'X', 'Y', 'Z');
					}
					
					static int hold(Depositor* ref, int x, Depositor* ref2, wchar_t xxxx, char16_t yyyy, char32_t zzzz)
					{
						dosomething(nullptr);
						doX(nullptr);
						return x * 2;
					}
			};
			
			class Figure : HasAnnotation, Depositor
			{
				private:
		
					int x;
					int y;
			
					void privateMethod()
					{
						x = increment(7);
					}

	
				public:
					
					int z;
		
					Figure(int x, int y)
					{
						this->x = x;
						this->y = y;
					}
		
					void publicMethod()
					{
						this->privateMethod();
					}
			};
		}
	}
	
	void doX(com::stormmq::inheritance::Depositor* ref)
	{
		
	}
	
}

void dosomething(com::stormmq::inheritance::Depositor* ref)
{
}


using namespace com::stormmq::inheritance;

int main()
{
	Figure* figure = new Figure(3, 10);
	Figure figures = *figure;

	figure->publicMethod();
	
	return 0;
}
