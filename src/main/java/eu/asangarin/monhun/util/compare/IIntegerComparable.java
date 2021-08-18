package eu.asangarin.monhun.util.compare;

import org.jetbrains.annotations.NotNull;

public interface IIntegerComparable extends Comparable<IIntegerComparable> {
	@Override
	default int compareTo(@NotNull IIntegerComparable o) {
		return Integer.compare(getComparableInt(), o.getComparableInt());
	}

	int getComparableInt();
}
