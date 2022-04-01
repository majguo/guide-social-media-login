// tag::copyright[]
/*******************************************************************************
 * Copyright (c) 2020, 2022 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - Initial implementation
 *******************************************************************************/
// end::copyright[]
package io.openliberty.guides.sociallogin;

import io.openliberty.guides.sociallogin.logout.ILogout;
import io.openliberty.guides.sociallogin.logout.LogoutHandler;

import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.HttpConstraint;
import jakarta.servlet.annotation.ServletSecurity;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.Response;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "LogoutServlet", urlPatterns = "/logout")
@ServletSecurity(value = @HttpConstraint(rolesAllowed = {"users"},
        transportGuarantee = ServletSecurity.TransportGuarantee.CONFIDENTIAL))
public class LogoutServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // tag::inject[]
    @Inject
    private LogoutHandler logoutHandler;
    //end::inject[]

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        // tag::getLogout[]
        ILogout logout = logoutHandler.getLogout();
        // end::getLogout[]
        // tag::revoke[]
        Response logoutResponse = logout.logout();
        // end::revoke[]

        // tag::errorHandle[]
        Response.Status.Family responseCodeFamily = logoutResponse
                .getStatusInfo()
                .getFamily();
        if (!responseCodeFamily.equals(Response.Status.Family.SUCCESSFUL)) {
            Logger.getLogger("LogoutServlet").log(Level.SEVERE,
                    logoutResponse.readEntity(Map.class).toString());
            throw new ServletException("Could not delete OAuth2 application grant");
        }
        // end::errorHandle[]

        // tag::logout[]
        request.logout();
        // end::logout[]

        // tag::redirect[]
        response.sendRedirect("hello.html");
        // end::redirect[]
    }
}
