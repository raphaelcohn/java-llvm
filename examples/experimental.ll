%class.com.stormmq.OtherClass = type opaque
%class.com.stormmq.MyClassExample = type
{
	i32,
	%class.com.stormmq.MyClassExample *,
	%class.com.stormmq.OtherClass *,
	double
}

@.arrayindex.length.com.stormmq.MyClassExample = constant %class.com.stormmq.MyClassExample *
	getelementptr (%class.com.stormmq.MyClassExample, %class.com.stormmq.MyClassExample * null, i64 1)
