package reference;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;

public class AlertOperator {

    public String verifyHint(String hintText, WebDriver driver)
    {
        List<WebElement> tempWebElementList;//临时的web元素列表
        driver.findElement(By.className("outer")).click();
        try {
            tempWebElementList = driver.findElement(By.className("outer")).findElements(By.tagName("span"));
            String verifyText ="ii" ;
            for(int i = 0; i < tempWebElementList.size();i++)
            {
                verifyText = tempWebElementList.get(i).getText();
                if(verifyText.equals(hintText))
                {
                    System.out.println("hintText " + hintText );
                    System.out.println("verifyText " + verifyText );
                    assertEquals("网页提示信息不正确",verifyText, hintText);
                }
            }
            assertEquals("网页提示信息不正确",verifyText, hintText);
            return verifyText;
        }
        catch (NoSuchElementException e){
            return null;
        }

    }
}