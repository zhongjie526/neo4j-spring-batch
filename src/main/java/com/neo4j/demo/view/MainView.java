package com.neo4j.demo.view;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

@PageTitle("Main")
@Route(value = "")
public class MainView extends VerticalLayout {


    public MainView() {



        RouterLink ex1 = new RouterLink("Example 1",Example1.class);
        RouterLink ex2 = new RouterLink("Example 2",Example2.class);

        setMargin(true);

        add(ex1,ex2);

        HorizontalLayout layout = new HorizontalLayout();
        layout.add(new Button("Example 4"));
        layout.add(new Button("Example 5"));
        add(layout);

    }

}