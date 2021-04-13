import org.junit.Test;
import  org.junit.Assert;

public class ElevatorDistributorTest {

    @Test
    public void chooseElevatorWithNoTasks(){
        // given
        int ELEVATOR_NUMBER = 2;
        ElevatorDistributor elevatorDistributor = new ElevatorDistributor(ELEVATOR_NUMBER);

        // when
        elevatorDistributor.getNumber(2, 5);

        // then
        Assert.assertEquals(1, elevatorDistributor.getNumber(3, 2));
    }

    @Test
    public void chooseElevatorWithShorterWholeDistance(){
        // given
        int ELEVATOR_NUMBER = 2;
        ElevatorDistributor elevatorDistributor = new ElevatorDistributor(ELEVATOR_NUMBER);

        // when
        elevatorDistributor.getNumber(2, 100); // gave the task to the elevator number 1
        elevatorDistributor.getNumber(1, 5); // gave the task to the elevator number 0
        elevatorDistributor.getNumber(6, 7); // gave the task to the elevator number 0
        // then
        Assert.assertEquals(0, elevatorDistributor.getNumber(3, 2));
    }

    @Test
    public void doNotCallElevatorWhenOtherIsComingToTheFloor(){
        // given
        int ELEVATOR_NUMBER = 3;
        ElevatorDistributor elevatorDistributor = new ElevatorDistributor(ELEVATOR_NUMBER);

        // when
        elevatorDistributor.getNumber(2, 100);
        elevatorDistributor.getNumber(1, 5);

        // then
        Assert.assertEquals(1, elevatorDistributor.getNumber(5, 2));
    }

    @Test
    public void getExistingElevatorWithTaskStatus(){
        // given
        int ELEVATOR_NUMBER = 1;
        ElevatorDistributor elevatorDistributor = new ElevatorDistributor(ELEVATOR_NUMBER);

        // when
        elevatorDistributor.getNumber(2, 100);

        // then
        Assert.assertEquals("ELEVATOR ID: 0 CURRENT FLOOR: 0 DESTINATION FLOOR: 2", elevatorDistributor.getElevatorStatus(0));
    }

    @Test
    public void getExistingElevatorWithNoTasksStatus(){
        // given
        int ELEVATOR_NUMBER = 1;
        ElevatorDistributor elevatorDistributor = new ElevatorDistributor(ELEVATOR_NUMBER);

        // when

        // then
        Assert.assertEquals("ELEVATOR ID: 0 CURRENT FLOOR: 0 DESTINATION FLOOR: 0", elevatorDistributor.getElevatorStatus(0));
    }

    @Test
    public void getNonExistingElevatorStatus(){
        // given
        int ELEVATOR_NUMBER = 1;
        ElevatorDistributor elevatorDistributor = new ElevatorDistributor(ELEVATOR_NUMBER);

        // when

        // then
        Assert.assertEquals("THERE IS NO ELEVATOR WITH SUCH ID", elevatorDistributor.getElevatorStatus(12));
    }
}
