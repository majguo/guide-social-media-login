// tag::copyright[]
/*******************************************************************************
 * Copyright (c) 2020, 2022 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
// end::copyright[]
package io.openliberty.guides.sociallogin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.HttpConstraint;
import jakarta.servlet.annotation.ServletSecurity;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.ibm.websphere.security.social.UserProfileManager;
import java.util.List;

@WebServlet(name = "HelloServlet", urlPatterns = "/hello")
@ServletSecurity(value = @HttpConstraint(rolesAllowed = {"users"},
        transportGuarantee = ServletSecurity.TransportGuarantee.CONFIDENTIAL))
public class HelloServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        
        @SuppressWarnings("unchecked")
        List<String> groups = UserProfileManager.getUserProfile().getIdToken().getClaims().getClaim("groups",
                List.class);
        System.out.println(groups);

        String username = request.getUserPrincipal().getName();
        request.setAttribute("username", username);

        // tag::serve[]
        request
                .getRequestDispatcher("securedHello.jsp")
                .forward(request, response);
        // end::serve[]
    }
}
