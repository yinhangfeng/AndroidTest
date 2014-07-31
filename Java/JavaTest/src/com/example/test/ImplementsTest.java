package com.example.test;

interface A {
	void aa();
	void bb();
}

interface B {
	void aa();
	void bb();
}

public class ImplementsTest implements A, B {

	@Override
	public void aa() {
		U.println("aa");
		
	}

	@Override
	public void bb() {
		U.println("bb");
		
	}
	
	public static void test() {
		ImplementsTest test = new ImplementsTest();
		A a = test;
		B b = test;
		test.aa();
		a.aa();
		b.aa();
	}

}
