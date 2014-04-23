/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package threes.listener;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import threes.main.ThreesFrame;

/**
 *
 * @author Legend
 */
public class MouseListener extends MouseAdapter{
    private ThreesFrame mainFrame;
    private int count = 0;
    private DragHandler handler;
    
    public MouseListener(ThreesFrame frame){
        this.mainFrame = frame;
        this.handler = new DragHandler(mainFrame);
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        handler.mouseRelease(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        handler.mousePress(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        handler.mouseDragged(e);
    }
}
