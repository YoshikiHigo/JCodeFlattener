package yoshikihigo.jcf.testcases;

public class TypeParameterTest1<T> {

	T a;

	void set(T a) {
		this.a = a;
	}

	T get() {
		return this.a;
	}

	void print() {
		System.out.println(this.get());
	}
}
