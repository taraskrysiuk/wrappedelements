package decorator;

import decorator.handlers.LocatingElementListProxyHandler;
import elements.BaseElement;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.internal.WrapsElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.FieldDecorator;
import org.openqa.selenium.support.pagefactory.internal.LocatingElementHandler;
import org.openqa.selenium.support.pagefactory.internal.LocatingElementListHandler;

import java.lang.reflect.*;
import java.util.List;

public class Decorator implements FieldDecorator {

    private final DefaultElementLocatorFactory factory;

    public Decorator(SearchContext searchContext){
        factory = new DefaultElementLocatorFactory(searchContext);
    }

    public Object decorate(ClassLoader classLoader, Field field) {
        if (isListOfWrappedElements(field) && hasAnnotation(field)) {
            return proxyForListWrappedElements(classLoader, factory.createLocator(field), wrapListClass(field));
        }
        if (isWrappedElement(field) && hasAnnotation(field)) {
            return proxyForWrappedElement(classLoader, factory.createLocator(field), field.getType());
        }
        if (WebElement.class.isAssignableFrom(field.getType()) && hasAnnotation(field)) {
            return proxyForWebElement(classLoader, factory.createLocator(field));
        }
        if (List.class.isAssignableFrom(field.getType()) && isDecoratableList(field) && hasAnnotation(field)){
            return proxyForListWebElements(classLoader, factory.createLocator(field));
        }
        return null;
    }

    private boolean isWrappedElement(Field field){
        return BaseElement.class.isAssignableFrom(field.getType()) ? true : false;
    }

    private boolean isListOfWrappedElements(Field field){
        return List.class.isAssignableFrom(field.getType()) ?
                BaseElement.class.isAssignableFrom((Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0]) :
                false;
    }

    private <T> List<T> proxyForListWrappedElements(ClassLoader classLoader, ElementLocator locator, Class<T> type){
        return (List<T>) Proxy.newProxyInstance(classLoader, new Class[] {List.class}, new LocatingElementListProxyHandler(locator, type));
    }

    private <T> T proxyForWrappedElement(ClassLoader classLoader, ElementLocator locator, Class<T> type){
        try {
            return type.getConstructor(WebElement.class).newInstance((T) Proxy.newProxyInstance(classLoader, new Class[]{WebElement.class}, new LocatingElementHandler(locator)));
        } catch (Exception e) {
            return null;
        }
    }

    private WebElement proxyForWebElement(ClassLoader loader, ElementLocator locator) {
        return (WebElement) Proxy.newProxyInstance(loader, new Class[]{WebElement.class, WrapsElement.class, Locatable.class}, new LocatingElementHandler(locator));
    }

    private List<WebElement> proxyForListWebElements(ClassLoader loader, ElementLocator locator) {
        return (List<WebElement>) Proxy.newProxyInstance(loader, new Class[]{List.class}, new LocatingElementListHandler(locator));
    }

    private boolean isDecoratableList(Field field) {
        return List.class.isAssignableFrom(field.getType()) ?
                WebElement.class.isAssignableFrom((Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0]) :
                false;
    }

    private Class wrapListClass(Field field){
        Type genericType = field.getGenericType();
        if (!(genericType instanceof ParameterizedType)) {
            return null;
        }
        return (Class) ((ParameterizedType) genericType).getActualTypeArguments()[0];
    }

    private boolean hasAnnotation(Field field){
        if (field.getAnnotation(FindBy.class) == null &&
                field.getAnnotation(FindBys.class) == null &&
                field.getAnnotation(FindAll.class) == null) {
            return false;
        } else {
            return true;
        }
    }
}
