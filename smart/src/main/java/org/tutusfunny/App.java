package org.tutusfunny;

import javax.swing.*;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        SwingUtilities.invokeLater(() -> {
            SocialMediaAnalyzer analyzer = new SocialMediaAnalyzer();
            analyzer.setVisible(true);
        });
    }

}
