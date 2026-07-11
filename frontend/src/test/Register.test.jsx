import { render, screen } from "@testing-library/react";
import { BrowserRouter } from "react-router-dom";
import Register from "../pages/Register";
import { AuthProvider } from "../context/AuthContext";

test("renders register form", () => {

    render(
        <BrowserRouter>
            <AuthProvider>
                <Register />
            </AuthProvider>
        </BrowserRouter>
    );

    expect(
        screen.getByPlaceholderText("Name")
    ).toBeInTheDocument();

    expect(
        screen.getByPlaceholderText("Email")
    ).toBeInTheDocument();

    expect(
        screen.getByPlaceholderText("Password")
    ).toBeInTheDocument();

    expect(
        screen.getByRole("button", {
            name: /register/i
        })
    ).toBeInTheDocument();

});