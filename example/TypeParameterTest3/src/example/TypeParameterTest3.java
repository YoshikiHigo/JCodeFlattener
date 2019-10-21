package example;

import java.util.ArrayList;
import java.util.List;

public class TypeParameterTest3<T extends String> {

	public List<T> method1() {
		return new ArrayList<T>();
	}
}
