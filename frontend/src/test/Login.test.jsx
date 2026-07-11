import { render, screen } from "@testing-library/react";
import { BrowserRouter } from "react-router-dom";

import Login from "../pages/Login";
import { AuthProvider } from "../context/AuthContext";

test("renders login form", () => {

    render(

        <BrowserRouter>

            <AuthProvider>

                <Login />

            </AuthProvider>

        </BrowserRouter>

    );

    expect(
        screen.getByPlaceholderText("Email")
    ).toBeInTheDocument();

    expect(
        screen.getByPlaceholderText("Password")
    ).toBeInTheDocument();

    expect(
        screen.getByRole("button", {
            name: /login/i,
        })
    ).toBeInTheDocument();

});