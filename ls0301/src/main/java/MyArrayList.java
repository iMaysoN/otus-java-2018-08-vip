import java.util.*;
import java.util.function.Consumer;

public class MyArrayList<E> implements Collection<E> {
    private final static int defaultCapacity = 10;
    private final static int defaultGrow = 2;
    private static final Object[] emptyElements = {};
    private int capacity;
    private int size;
    private Object[] elements;

    public MyArrayList() {
        this.capacity = defaultCapacity;
        this.elements = new Object[capacity];
        this.size = 0;
    }

    public MyArrayList(final int initialSize) {
        if (initialSize < 0) {
            throw new IllegalArgumentException("Initial size can't be lower than 0.");
        }
        this.capacity = initialSize;
        if (initialSize == 0) {
            this.elements = emptyElements;
        } else {
            this.elements = new Object[capacity];
        }
        this.size = 0;
    }

    public MyArrayList(final Collection<? extends E> initialCollection) {
        this.elements = initialCollection.toArray();
        this.size = initialCollection.size();
        this.capacity = size * defaultGrow;
    }

    public boolean add(final E e) {
        if (size >= capacity) {
            final int newCapacity = capacity * defaultGrow;
            final Object[] newElements = new Object[newCapacity];
            System.arraycopy(elements, 0, newElements, 0, size);
            this.elements = newElements;
            this.capacity = newCapacity;
        }
        elements[size] = e;
        size++;
        return true;
    }

    public boolean remove(final Object e) {
        int index = indexOf(e);
        if (index < 0) {
            return false;
        }
        if (index == (capacity - 1)) {
            elements[index] = null;
        } else {
            System.arraycopy(elements, index + 1, elements, index, size - index - 1);
        }
        size--;
        return true;
    }

    public boolean addAll(final Collection<? extends E> c) {
        c.forEach(this::add);
        return true;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public boolean contains(final Object o) {
        return indexOf(o) >= 0;
    }

    public int indexOf(final Object o) {
        if (o != null) {
            for (int i = 0; i < size; i++) {
                if (elements[i].equals(o)) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < size; i++)
                if (elements[i] == null)
                    return i;
        }
        return -1;
    }

    public Iterator<E> iterator() {
        return new Itr();
    }

    private transient int modCount = 0;

    private class Itr implements Iterator<E> {
        int cursor;       // index of next element to return
        int lastRet = -1; // index of last element returned; -1 if no such
        int expectedModCount = modCount;

        // prevent creating a synthetic constructor
        Itr() {
        }

        public boolean hasNext() {
            return cursor != size;
        }

        @SuppressWarnings("unchecked")
        public E next() {
            checkForComodification();
            int i = cursor;
            if (i >= size)
                throw new NoSuchElementException();
            Object[] elementData = MyArrayList.this.elements;
            if (i >= elementData.length)
                throw new ConcurrentModificationException();
            cursor = i + 1;
            return (E) elementData[lastRet = i];
        }

        public void remove() {
            if (lastRet < 0)
                throw new IllegalStateException();
            checkForComodification();

            try {
                MyArrayList.this.remove(lastRet);
                cursor = lastRet;
                lastRet = -1;
                expectedModCount = modCount;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        @Override
        public void forEachRemaining(Consumer<? super E> action) {
            Objects.requireNonNull(action);
            final int size = MyArrayList.this.size;
            int i = cursor;
            if (i < size) {
                final Object[] es = elements;
                if (i >= es.length)
                    throw new ConcurrentModificationException();
                for (; i < size && modCount == expectedModCount; i++)
                    action.accept(elementAt(es, i));
                // update once at end to reduce heap write traffic
                cursor = i;
                lastRet = i - 1;
                checkForComodification();
            }
        }

        final void checkForComodification() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
        }
    }

    @SuppressWarnings("unchecked")
    private static <E> E elementAt(Object[] es, int index) {
        return (E) es[index];
    }

    public E[] toArray() {
        return (E[]) elements;
    }

    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    public boolean retainAll(Collection c) {
        return false;
    }

    public boolean removeAll(Collection c) {
        final Object[] arrayToCheck = c.toArray();
        final int initialSize = size;
        for (int i = 0; i < arrayToCheck.length; i++) {
            Object currentElement = arrayToCheck[i];
            if (indexOf(currentElement) >= 0) {
                remove(currentElement);
            }
        }
        return initialSize != size;
    }

    public boolean containsAll(Collection c) {
        for (Object obj : c) {
            if (!contains(c)) {
                return false;
            }
        }
        return true;
    }

    public Object[] toArray(Object[] a) {
        return elements;
    }

    @Override
    public String toString() {
        Iterator<E> it = iterator();
        if (!it.hasNext())
            return "[]";

        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (; ; ) {
            E e = it.next();
            sb.append(e == this ? "(this Collection)" : e);
            if (!it.hasNext())
                return sb.append(']').toString();
            sb.append(',').append(' ');
        }
    }
}
