package amiltone.bsaugues.bluetoothapp.data.entity;

/**
 * Created by amiltonedev_dt013 on 26/09/2017.
 */

public class BluetoothCommandResult {

    private Result result;

    public enum Result{
        SUCCESS,
        FAIL
    }

    public BluetoothCommandResult(Result result) {
        this.result = result;

    }

    public Result getResult() {
        return result;
    }
}
