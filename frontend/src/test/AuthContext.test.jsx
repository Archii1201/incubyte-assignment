import { render, screen } from "@testing-library/react";
import { AuthProvider } from "../context/AuthContext";

test("auth provider renders children", () => {

    render(

        <AuthProvider>

            <div>Test Child</div>

        </AuthProvider>

    );

    expect(
        screen.getByText("Test Child")
    ).toBeInTheDocument();

});