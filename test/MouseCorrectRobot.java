package test;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;

public class MouseCorrectRobot extends Robot
{
    final Dimension ScreenSize;// Primary Screen Size

    public MouseCorrectRobot() throws AWTException
    {
        super();
        ScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
    }

    private static double getTav(Point a, Point b)
    {
        return Math.sqrt((double) ((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y)));
    }


    public void MoveMouseControlled(double xbe, double ybe)// Position of the cursor in [0,1] ranges. (0,0) is the upper left corner
    {

        int xbepix = (int) (ScreenSize.width * xbe);
        int ybepix = (int) (ScreenSize.height * ybe);

        int x = xbepix;
        int y = ybepix;

        Point mert = MouseInfo.getPointerInfo().getLocation();
        Point ElozoInitPont = new Point(0, 0);

        int UgyanAztMeri = 0;
        final int UgyanAZtMeriLimit = 30;

        int i = 0;
        final int LepesLimit = 20000;
        while ((mert.x != xbepix || mert.y != ybepix) && i < LepesLimit && UgyanAztMeri < UgyanAZtMeriLimit)
        {
            ++i;
            if (mert.x < xbepix)
                ++x;
            else
                --x;
            if (mert.y < ybepix)
                ++y;
            else
                --y;
            mouseMove(x, y);

            mert = MouseInfo.getPointerInfo().getLocation();

            if (getTav(ElozoInitPont, mert) < 5)
                ++UgyanAztMeri;
            else
            {
                UgyanAztMeri = 0;
                ElozoInitPont.x = mert.x;
                ElozoInitPont.y = mert.y;
            }

        }
    }

}