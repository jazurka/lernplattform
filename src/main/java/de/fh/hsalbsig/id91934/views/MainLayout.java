package de.fh.hsalbsig.id91934.views;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.theme.lumo.LumoUtility;
import de.fh.hsalbsig.id91934.services.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.lineawesome.LineAwesomeIcon;

/**
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout {

  private H1 viewTitle;
  private SecurityService securityService;

  /**
   * The view creating all the components.
   * 
   * <p>@param securityService gets autowired to handle login and authorization
   */
  public MainLayout(@Autowired SecurityService securityService) {
    this.securityService = securityService;

    setPrimarySection(Section.DRAWER);
    addDrawerContent();
    addHeaderContent();
  }

  private void addHeaderContent() {
    DrawerToggle toggle = new DrawerToggle();
    toggle.setAriaLabel("Menu toggle");

    viewTitle = new H1();
    viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

    HorizontalLayout header;

    if (securityService.getAuthenticatedUser() != null) {
      Button logout = new Button("Logout", click -> securityService.logout());
      logout.addClassNames(LumoUtility.Margin.End.LARGE);
      header = new HorizontalLayout(viewTitle, logout);
    } else {
      header = new HorizontalLayout(viewTitle);
    }

    header.setSizeFull();
    header.setAlignItems(FlexComponent.Alignment.CENTER);
    header.expand(viewTitle);

    addToNavbar(true, toggle, header);
  }

  private void addDrawerContent() {
    Span appName = new Span("Lernplattform");
    appName.addClassNames(LumoUtility.FontWeight.SEMIBOLD, LumoUtility.FontSize.XXLARGE);
    Header header = new Header(appName);

    Scroller scroller = new Scroller(createNavigation());

    addToDrawer(header, scroller, createFooter());
  }

  private SideNav createNavigation() {
    SideNav nav = new SideNav();

    nav.addItem(
        new SideNavItem("HelloWorld", HelloWorldView.class, LineAwesomeIcon.HOME_SOLID.create()));
    nav.addItem(
        new SideNavItem("Admin Panel", AdminPanelView.class, LineAwesomeIcon.COG_SOLID.create()));

    return nav;
  }

  private Footer createFooter() {
    Footer layout = new Footer();

    return layout;
  }

  @Override
  protected void afterNavigation() {
    super.afterNavigation();
    viewTitle.setText(getCurrentPageTitle());
  }

  private String getCurrentPageTitle() {
    PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
    return title == null ? "" : title.value();
  }
}
