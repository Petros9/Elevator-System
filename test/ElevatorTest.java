import org.junit.Test;
import  org.junit.Assert;
public class ElevatorTest {

    @Test
    public void getWholeDistance(){
        // given
        Elevator elevator = new Elevator(1, 1);

        // when
        elevator.setDestination(2);
        elevator.setDestination(13);
        elevator.setDestination(6);
        elevator.setDestination(1);
        elevator.setDestination(8);
        elevator.setDestination(7);
        elevator.setDestination(8);
        // then
        Assert.assertEquals(33, elevator.getWholeDistance());
    }

    @Test
    public void getLastDestination(){
        //given
        Elevator elevator = new Elevator(1, 1);

        //when
        elevator.setDestination(2);
        elevator.setDestination(13);

        //then
        Assert.assertEquals(13, elevator.getLastDestination());
    }
}
