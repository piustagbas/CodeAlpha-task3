package org.tutusfunny;

import com.restfb.*;
import com.restfb.types.Page;
import com.restfb.types.Post;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;

public class SocialMediaAnalyzer extends JFrame {

    private static final String ACCESS_TOKEN = "EAADZBiIin2SYBOwNzpEQfyaMAZCy8A4NCTaKIKEZAwoFIf3E6ZBCHDWlkgcq0yTkWmGeBpVCYAaub6TZCxSoshgkcCrh18cmziZBpnhS3znLu6u522qgI2PNvl2gqN1iM9WtJBu6HkgZBlIfNuAvlN2ZCpn5Nb58FYbtwKC2BHZCpECYcuCMCEIhLMqeFYvyor1turRJsPNbiSNZAZCcZBvgA0FKAzHyZBJrUsf2gRxD73GIhLyTM1PVgAqFrZB5O4ViMaqgZDZD";
    private static final String PAGE_ID = "764733208798021";

    public SocialMediaAnalyzer() {
        initUI();
    }

    private void initUI() {
        System.out.println("Initializing UI");
        setTitle("Social Media Analyzer");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);


        SwingUtilities.invokeLater(() -> {
            System.out.println("Fetching data");
            DefaultPieDataset dataset = fetchData();
            System.out.println("Data fetched");
            JFreeChart chart = createChart(dataset);
            System.out.println("Chart created Successfully");
            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(560, 370));
            setContentPane(chartPanel);
        });
    }

    private DefaultPieDataset fetchData() {
        try {
            System.out.println("Connecting to Facebook");
            FacebookClient fbClient = new DefaultFacebookClient(ACCESS_TOKEN, Version.LATEST);
            Page page = fbClient.fetchObject(PAGE_ID, Page.class);

            Long fanCount = page.getFanCount();
            int likes = (fanCount != null) ? fanCount.intValue() : 0;

            Long talkingAboutCount = page.getTalkingAboutCount();
            int talkingAbout = (talkingAboutCount != null) ? talkingAboutCount.intValue() : 0;

            Connection<Post> posts = fbClient.fetchConnection(page.getId() + "/posts", Post.class,
                    Parameter.with("limit", 100));

            int postCount = posts.getData().size();

            return createDataset(likes, talkingAbout, postCount);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error fetching data: " + e.getMessage());

            return new DefaultPieDataset();
        }
    }

    private DefaultPieDataset createDataset(int likes, int talkingAbout, int postCount) {
        System.out.println("Creating dataset");
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Likes", likes);
        dataset.setValue("Talking About", talkingAbout);
        dataset.setValue("Posts", postCount);
        return dataset;
    }

    private JFreeChart createChart(DefaultPieDataset dataset) {
        System.out.println("Creating chart");
        JFreeChart chart = ChartFactory.createPieChart(
                "Social Media Insights",
                dataset,
                true,
                true,
                false
        );

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setSectionPaint("Likes", new Color(0, 128, 0));
        plot.setSectionPaint("Talking About", new Color(255, 165, 0));
        plot.setSectionPaint("Posts", new Color(0, 0, 255));

        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}: {1} ({2})", NumberFormat.getNumberInstance(), NumberFormat.getPercentInstance()));

        plot.setLabelFont(new Font("SansSerif", Font.BOLD, 12));


        chart.getLegend().setItemFont(new Font("SansSerif", Font.PLAIN, 12));
        chart.getLegend().setPosition(RectangleEdge.BOTTOM);


        chart.setBackgroundPaint(new Color(240, 240, 240)); // Light Gray

        return chart;
    }
}
