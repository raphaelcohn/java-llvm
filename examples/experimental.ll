%class.com.stormmq.OtherClass = type opaque
%class.com.stormmq.MyClassExample = type
{
	i32,
	%class.com.stormmq.MyClassExample *,
	%class.com.stormmq.OtherClass *,
	double
}

@.arrayindex.length.com.stormmq.MyClassExample = constant %class.com.stormmq.MyClassExample *
	getelementptr inbounds (%class.com.stormmq.MyClassExample, %class.com.stormmq.MyClassExample * null, i64 1)

; constant expression
; rule 1:
;   getelementptr %class.com.stormmq.MyClassExample, %class.com.stormmq.MyClassExample * null, i64 1
;   goes to
;	getelementptr (%class.com.stormmq.MyClassExample, %class.com.stormmq.MyClassExample * null, i64 1)
; rule 2 (embedded in another rule)
;   subexpresison, eg
;	getelementptr (%class.com.stormmq.MyClassExample, %class.com.stormmq.MyClassExample * null, i64 1)
;   goes to
;   (CAST TYPE xxxxx), ie 'return type' of the constant expression
@.sizeof.com.stormmq.MyClassExample = constant i64 ptrtoint (%class.com.stormmq.MyClassExample * getelementptr inbounds (%class.com.stormmq.MyClassExample, %class.com.stormmq.MyClassExample * null, i64 1) to i64)

@.offsetof.3.com.stormmq.MyClassExample = constant i32 ptrtoint (double * getelementptr (%class.com.stormmq.MyClassExample, %class.com.stormmq.MyClassExample * null, i64 0, i32 3) to i32)


%Struct = type { i8, i32, i8* }
@Struct_size = constant i32 ptrtoint (%Struct* getelementptr (%Struct, %Struct* null, i32 1) to i32)

	