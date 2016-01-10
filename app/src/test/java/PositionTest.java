import com.mytway.pojo.Position;
import org.junit.Test;
import static org.junit.Assert.*;
import com.mytway.pojo.Position;

public class PositionTest {

    @Test
    public void testTest(){
        Position home = new Position(20.15, 50.15);
        Position work = new Position(20.15, 50.15);

        if(home.equals(work)){
            System.out.println("To samo");
        }else{
            System.out.printf("Nie to samo");
        }

        boolean test = 20.15 == 20.15;
        System.out.println(" Nadal");
    }
}
