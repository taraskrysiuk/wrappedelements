package pageobject;

import decorator.Decorator;
import elements.BaseElement;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public abstract class BaseSection extends BaseElement {

    public BaseSection(WebElement rootElement) {
        super(rootElement);
        PageFactory.initElements(new Decorator(rootElement), this);
    }
}
