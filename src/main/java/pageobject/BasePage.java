package pageobject;

import decorator.Decorator;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.support.PageFactory;

public abstract class BasePage {

    public BasePage(SearchContext searchContext) {
        PageFactory.initElements(new Decorator(searchContext), this);
    }
}
