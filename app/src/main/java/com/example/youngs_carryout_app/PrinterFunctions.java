package com.example.youngs_carryout_app;

import android.graphics.Bitmap;
import com.starmicronics.starioextension.ICommandBuilder;
import com.starmicronics.starioextension.StarIoExt;
import java.util.List;

import com.starmicronics.starioextension.StarIoExt.Emulation;

public class PrinterFunctions {

    public static byte[] createRasterData(Emulation emulation, Bitmap bitmap, int width, boolean bothScale) {
        ICommandBuilder builder = StarIoExt.createCommandBuilder(emulation);

        builder.beginDocument();

        builder.appendBitmap(bitmap, true, width, bothScale);

        builder.appendCutPaper(ICommandBuilder.CutPaperAction.PartialCutWithFeed);

        builder.endDocument();

        return builder.getCommands();
    }

    // portNameSearch should be BT:
    public static String getFirstPrinter(String portNameSearch) {
        String portName = "";
        List<com.starmicronics.stario.PortInfo> portList;
        try {
            portList = com.starmicronics.stario.StarIOPort.searchPrinter(portNameSearch);

            for (com.starmicronics.stario.PortInfo portInfo : portList) {
                portName = portInfo.getPortName();
                break;
            }
        }
        catch (com.starmicronics.stario.StarIOPortException ex) {
            ex.printStackTrace();
        }
        return portName;
    }
}
