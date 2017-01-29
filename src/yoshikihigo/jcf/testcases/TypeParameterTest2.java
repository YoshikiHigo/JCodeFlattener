package yoshikihigo.jcf.testcases;

public class TypeParameterTest2 {

	void method1() {
		final TypeParameterTest1<String> t1 = new TypeParameterTest1<String>();
		t1.set("ABC");
		System.out.println(t1.get());
	}
}
