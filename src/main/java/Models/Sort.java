package Models;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class Sort {

    public void sort(Object toBeSorted, Method method, boolean isAscending) {
        ArrayList<Object> object = new ArrayList<>();
        object = ((ArrayList<Object>) toBeSorted);
        String type;
        try {
            type = method.invoke(object.get(0)).getClass().getSimpleName();
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return;
        }
        if (type.equalsIgnoreCase("Integer"))
            sortInteger(object, method, isAscending);
        else if (type.equalsIgnoreCase("Long"))
            sortLong(object, method, isAscending);
        else if (type.equalsIgnoreCase("String"))
            sortString(object, method, isAscending);
        else if (type.equalsIgnoreCase("Double"))
            sortDouble(object, method, isAscending);
        else if(type.equalsIgnoreCase("Date"))
            sortDate(object, method, isAscending);
    }

    private void sortInteger(ArrayList<Object> toBeSorted, Method method, boolean isAscending) {
        Collections.sort(toBeSorted, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                try {
                    if (isAscending)
                        return ((int) method.invoke(o1)) - ((int) method.invoke(o2));
                    else
                        return ((int) method.invoke(o2)) - ((int) method.invoke(o1));
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                    System.exit(1);
                }
                return 0;
            }
        });
    }

    private void sortString(ArrayList<Object> toBeSorted, Method method, boolean isAscending) {
        Collections.sort(toBeSorted, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                try {
                    if (isAscending)
                        return ((String) method.invoke(o1)).compareTo((String) method.invoke(o2));
                    else
                        return ((String) method.invoke(o2)).compareTo((String) method.invoke(o1));
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                    System.exit(1);
                }
                return 0;
            }
        });
    }

    private void sortLong(ArrayList<Object> toBeSorted, Method method, boolean isAscending) {
        Collections.sort(toBeSorted, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                try {
                    if (((long) method.invoke(o1)) == ((long) method.invoke(o2)))
                        return 0;
                    else if (isAscending)
                        return ((long) method.invoke(o1)) > ((long) method.invoke(o2)) ? 1 : -1;
                    else
                        return ((long) method.invoke(o2)) > ((long) method.invoke(o1)) ? 1 : -1;

                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                    System.exit(1);
                }
                return 0;
            }
        });
    }

    private void sortDouble(ArrayList<Object> toBeSorted, Method method, boolean isAscending) {
        Collections.sort(toBeSorted, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                try {
                    if (((double) method.invoke(o1)) == ((double) method.invoke(o2)))
                        return 0;
                    else if (isAscending)
                        return ((double) method.invoke(o1)) > ((double) method.invoke(o2)) ? 1 : -1;
                    else
                        return ((double) method.invoke(o2)) > ((double) method.invoke(o1)) ? 1 : -1;

                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                    System.exit(1);
                }
                return 0;
            }
        });

    }

    private void sortDate(ArrayList<Object> toBeSorted, Method method, boolean isAscending){
        Collections.sort(toBeSorted, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                try {
                     if (isAscending)
                        return ((Date) method.invoke(o1)).compareTo((Date) method.invoke(o2));
                    else
                        return ((Date) method.invoke(o2)).compareTo((Date) method.invoke(o1));

                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                    System.exit(1);
                }
                return 0;
            }
        });
    }


}
