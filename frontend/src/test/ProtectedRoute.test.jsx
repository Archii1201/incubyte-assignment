import { render, screen } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";
import { AuthContext } from "../context/AuthContext";
import ProtectedRoute from "../components/ProtectedRoute";

test("redirects unauthenticated user", () => {

    render(

        <MemoryRouter>

            <AuthContext.Provider
                value={{
                    user: null
                }}
            >

                <ProtectedRoute>

                    <h1>Dashboard</h1>

                </ProtectedRoute>

            </AuthContext.Provider>

        </MemoryRouter>

    );

    expect(
        screen.queryByText("Dashboard")
    ).not.toBeInTheDocument();

});