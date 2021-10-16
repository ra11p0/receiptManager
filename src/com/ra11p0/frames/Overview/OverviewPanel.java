package com.ra11p0.frames.Overview;

import com.ra11p0.frames.Overview.structures.Day;
import com.ra11p0.frames.ReceiptsManager.ReceiptsManager;
import com.ra11p0.structures.Receipt;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class OverviewPanel extends JPanel {
    private ArrayList<Day> days = new ArrayList<>();
    public OverviewPanel(){
        setLayout(new BorderLayout());
        setSize(800, 650);
        generateDaySet(new Date(System.currentTimeMillis()-(7*24 * 60 * 60 * 1000)), new Date(System.currentTimeMillis()));
        generateNavigationPanel();
        generateContentPanel();
        generateDataPanel();
    }
    private void generateNavigationPanel(){
        JPanel navigation = new JPanel(new BorderLayout());
        navigation.setBackground(Color.YELLOW);
        add(navigation, BorderLayout.PAGE_START);
        //*****
        Button previous = new Button("Previous");
        JLabel month = new JLabel("*Month placeholder*", SwingConstants.CENTER);
        Button next = new Button("Next");
        navigation.add(previous, BorderLayout.LINE_START);
        navigation.add(month, BorderLayout.CENTER);
        navigation.add(next, BorderLayout.LINE_END);
    }
    private void generateContentPanel(){
        //*****
        final int daysPerView = 7;
        //*****
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(Color.BLUE);
        add(content, BorderLayout.CENTER);
        //*****
        JPanel dates = new JPanel(new GridLayout(1, daysPerView));
        for(Day day : days){
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeZone(TimeZone.getTimeZone("GMT+1"));
            calendar.setTimeInMillis(day.get_date().getTime());
            dates.add(new JLabel(String.valueOf(calendar.get(Calendar.MONTH)+1) + " - " + String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)), SwingConstants.CENTER));
        }
        content.add(dates, BorderLayout.PAGE_START);
        //*****
        JPanel receiptsPanel = new JPanel(new GridLayout(1, daysPerView));
        ArrayList<JLabel> totalValues = new ArrayList<>();
        ArrayList<DefaultListModel<Receipt>> receiptListModels = new ArrayList<>();
        ArrayList<JPanel> jPanels = new ArrayList<>();
        for(int i=0; i<daysPerView;i++) {
            jPanels.add(new JPanel(new BorderLayout()));
            receiptsPanel.add(jPanels.get(jPanels.size()-1));
        }
        for(JPanel panel : jPanels){
            JList<Receipt> jList = new JList<>();
            JScrollPane scrollPane = new JScrollPane(jList);
            jList.addFocusListener(new FocusListener() {
                @Override
                public void focusGained(FocusEvent e) {

                }

                @Override
                public void focusLost(FocusEvent e) {
                    jList.clearSelection();
                }
            });
            totalValues.add(new JLabel(String.valueOf(days.get(receiptListModels.size()).get_total()), SwingConstants.CENTER));
            receiptListModels.add(new DefaultListModel<>());
            jList.setModel(receiptListModels.get(receiptListModels.size()-1));
            panel.add(scrollPane, BorderLayout.CENTER);
            panel.add(totalValues.get(totalValues.size()-1), BorderLayout.PAGE_END);
        }
        for(Day day : days)
        {
            if (day.get_receipts() != null) {
                for (Receipt receipt : day.get_receipts()) {
                    receiptListModels.get(days.indexOf(day)).addElement(receipt);
                }
            }
        }


        content.add(receiptsPanel, BorderLayout.CENTER);
        //*****
        JPanel controlButtons = new JPanel(new GridLayout(1, 4));
        Button edit = new Button("Edit");
        Button newReceipt = new Button("New receipt");
        Button remove = new Button("Remove");
        Button stats = new Button("Statistics");
        controlButtons.add(edit);
        controlButtons.add(newReceipt);
        controlButtons.add(remove);
        controlButtons.add(stats);
        content.add(controlButtons, BorderLayout.PAGE_END);
        //*****
    }
    private void generateDataPanel(){
        JPanel data = new JPanel(new GridLayout(2, 5));
        data.setBackground(Color.CYAN);
        add(data, BorderLayout.PAGE_END);
        //*****
        data.add(new JLabel("23% Tax", SwingConstants.CENTER));
        data.add(new JLabel("8% Tax", SwingConstants.CENTER));
        data.add(new JLabel("5% Tax", SwingConstants.CENTER));
        data.add(new JLabel("0% Tax", SwingConstants.CENTER));
        data.add(new JLabel("Total", SwingConstants.CENTER));
        data.add(new JLabel("alpha", SwingConstants.CENTER));
        data.add(new JLabel("beta", SwingConstants.CENTER));
        data.add(new JLabel("gamma", SwingConstants.CENTER));
        data.add(new JLabel("delta", SwingConstants.CENTER));
        data.add(new JLabel("epsilon", SwingConstants.CENTER));
    }
    private void generateDaySet(Date from, Date to){
        if(days.size() == 0){
            for(int i=0; i<7; i++){
                days.add(new Day(null, null));
            }
        }
        long diffInMs = Math.abs(to.getTime() - from.getTime());
        int dayCount = (int)TimeUnit.DAYS.convert(diffInMs, TimeUnit.MILLISECONDS);
        for(int i=0; i<dayCount; i++){
            Date currentlyProcessedDate = new Date((from.getTime() + ((long) (i+1) * 24 * 60 * 60 * 1000)));
            ArrayList<Receipt> currentDayReceipts = new ArrayList<>();
            for(Receipt receipt : ReceiptsManager.getReceipts()) {
                Calendar cal1 = Calendar.getInstance();
                Calendar cal2 = Calendar.getInstance();
                cal1.setTime(currentlyProcessedDate);
                cal2.setTime(receipt.get_date());
                boolean sameDay = cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
                        cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
                if (sameDay) {
                    currentDayReceipts.add(receipt);
                }
            }
            if (currentDayReceipts.size()!=0) days.set(i, new Day(currentlyProcessedDate, currentDayReceipts));
            else days.set(i, new Day(currentlyProcessedDate, null));
        }
    }
}
