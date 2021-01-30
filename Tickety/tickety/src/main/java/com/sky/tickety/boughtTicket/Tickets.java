package com.sky.tickety.boughtTicket;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sky.tickety.R;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

public class Tickets extends AppCompatActivity {


    TicketsAdapter ticketsAdapter;
    MPKBoughtTicketsAdapter mpkAdapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_tickets_recycle);
        recyclerView = findViewById(R.id.bought_recycle);
        mLayoutManager = new LinearLayoutManager(this);

        if(!getIntent().getStringExtra("data").equals("")) {

            List<List<String>> data = deserialize(getIntent().getStringExtra("data"));

            if(getIntent().getStringExtra("type").equals("MPK")) {
                mpkAdapter = new MPKBoughtTicketsAdapter(data, this);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mpkAdapter);
                mpkAdapter.notifyDataSetChanged();
            } else {
                ticketsAdapter = new TicketsAdapter(data, this);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(ticketsAdapter);
                ticketsAdapter.notifyDataSetChanged();
            }
        }
    }

    public static List<List<String>> deserialize(String xml) {

        final Document document;
        List<List<String>> dataList = new ArrayList<>();
        try {
            document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(xml.getBytes()));
            final XPathExpression xPathExpression = XPathFactory.newInstance().newXPath().compile("//data/text()");
            final NodeList nodeList = (NodeList) xPathExpression.evaluate(document, XPathConstants.NODESET);
            for (int i = 0; i < nodeList.getLength(); ++i) {
                dataList.add(new ArrayList<>(Arrays.asList(nodeList.item(i).getNodeValue().split("#"))));
            }
        } catch (IOException | SAXException | ParserConfigurationException | XPathExpressionException e) {
            e.printStackTrace();
        }
        return dataList;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}