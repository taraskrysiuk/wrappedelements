package decorator.handlers;

import elements.BaseElement;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

public class LocatingElementListProxyHandler<T extends BaseElement> implements InvocationHandler {

    private ElementLocator locator;
    private Class<T> wrapType;

    public LocatingElementListProxyHandler(ElementLocator locator, Class<T> wrapType) {
        this.locator = locator;
        this.wrapType = wrapType;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        List<T> elements = new LinkedList<>();
        for (WebElement element : locator.findElements()){
            elements.add(wrapType.getConstructor(WebElement.class).newInstance(element));
        }
        try{
            return method.invoke(elements, args);
        } catch (InvocationTargetException e){
            throw e.getCause();
        }
    }
}
