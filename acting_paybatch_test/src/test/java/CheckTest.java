import java.lang.reflect.Field;

public class CheckTest<T> {
    public Object getTempletAttr(T classIn,String attr) throws IllegalAccessException {
        Field[] fields=classIn.getClass().getDeclaredFields();
        boolean flag=false;
        int i;
        Object result = null;
        for (i = 0; i < fields.length; i++) {
            if(fields[i].getName().equals(attr))
            {
                flag = true;
                break;
            }
        }
        if(flag==true&&i<fields.length)
        {
            result = fields[i].get(classIn);

        }
        return result;
    }
}
