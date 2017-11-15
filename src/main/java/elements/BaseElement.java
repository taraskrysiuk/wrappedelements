package elements;

import org.openqa.selenium.*;
import org.openqa.selenium.internal.WrapsElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class BaseElement implements WebElement, WrapsElement {

    private WebElement wrappedElement;

    public BaseElement(WebElement wrappedElement) {
        this.wrappedElement = wrappedElement;
    }

    public void setWrappedElement(WebElement wrappedElement){
        this.wrappedElement = wrappedElement;
    }

    public void click() {
        wrappedElement.click();
    }

    public void submit() {
        wrappedElement.submit();
    }

    public void sendKeys(CharSequence... charSequences) {
        wrappedElement.sendKeys(charSequences);
    }

    public void clear() {
        wrappedElement.click();
    }

    public String getTagName() {
        return wrappedElement.getTagName();
    }

    public String getAttribute(String s) {
        return wrappedElement.getAttribute(s);
    }

    public boolean isSelected() {
        return wrappedElement.isSelected();
    }

    public boolean isEnabled() {
        return wrappedElement.isEnabled();
    }

    public String getText() {
        return wrappedElement.getText();
    }

    public List<WebElement> findElements(By by) {
        return wrappedElement.findElements(by);
    }

    public WebElement findElement(By by) {
        return wrappedElement.findElement(by);
    }

    public boolean isDisplayed() {
        return wrappedElement.isDisplayed();
    }

    public Point getLocation() {
        return wrappedElement.getLocation();
    }

    public Dimension getSize() {
        return wrappedElement.getSize();
    }

    public Rectangle getRect() {
        return wrappedElement.getRect();
    }

    public String getCssValue(String s) {
        return wrappedElement.getCssValue(s);
    }

    public <X> X getScreenshotAs(OutputType<X> outputType) throws WebDriverException {
        return wrappedElement.getScreenshotAs(outputType);
    }

    public WebElement getWrappedElement() {
        return wrappedElement;
    }
}
