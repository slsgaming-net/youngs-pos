package com.example.youngs_carryout_app;

import com.starmicronics.stario.StarIOPort;
import com.starmicronics.stario.StarIOPortException;
import com.starmicronics.stario.StarPrinterStatus;

@SuppressWarnings({"UnusedParameters", "UnusedAssignment", "WeakerAccess"})
public class Communication {
    @SuppressWarnings("unused")
    public enum Result {
        Success,
        ErrorUnknown,
        ErrorOpenPort,
        ErrorBeginCheckedBlock,
        ErrorEndCheckedBlock,
        ErrorWritePort,
        ErrorReadPort,
    }

    public static Result sendCommands(byte[] commands, String portName, String portSettings, int timeout) {
        Result result = Result.ErrorUnknown;

        StarIOPort port = null;
        try {
            result = Result.ErrorOpenPort;

            // open port
            port = StarIOPort.getPort(portName, "", 10000);

            StarPrinterStatus status;

            result = Result.ErrorBeginCheckedBlock;

            status = port.beginCheckedBlock();

            if (status.offline) {
                throw new StarIOPortException("A printer is offline");
            }

            result = Result.ErrorWritePort;

            port.writePort(commands, 0, commands.length);

            result = Result.ErrorEndCheckedBlock;

            port.setEndCheckedBlockTimeoutMillis(30000); // 3000ms

            status = port.endCheckedBlock();

            if (status.coverOpen) {
                throw new StarIOPortException("Receipt paper is empty");
            }
            else if (status.offline) {
                throw new StarIOPortException("Printer is offline");
            }

            result = Result.Success;

        }
        catch (StarIOPortException e) {
            // Nothing
        }
        finally {
            if (port != null) {
                try {
                    StarIOPort.releasePort(port);
                    port = null;
                }
                catch (StarIOPortException e) {
                    // Nothing
                }
            }
        }
        return result;
    }

    static Result sendCommands(byte[] commands, StarIOPort port) {
        Result result = Result.ErrorUnknown;

        try {
            if (port == null) {
                result = Result.ErrorOpenPort;
                return result;
            }

            StarPrinterStatus status;

            result = Result.ErrorWritePort;
            status = port.retreiveStatus();

            if (status.rawLength == 0) {
                throw new StarIOPortException("A printer is offline");
            }

            result = Result.ErrorWritePort;

            port.writePort(commands, 0, commands.length);

            result = Result.ErrorWritePort;
            status = port.retreiveStatus();

            if (status.rawLength == 0) {
                throw new StarIOPortException("A printer is offline");
            }

            result = Result.Success;

        }
        catch (StarIOPortException e) {
            // Nothing
        }
        return result;
    }
}
