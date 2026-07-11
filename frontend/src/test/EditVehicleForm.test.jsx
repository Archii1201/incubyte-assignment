import { render, screen } from "@testing-library/react";
import { MemoryRouter, Routes, Route } from "react-router-dom";
import { vi } from "vitest";
import EditVehicleForm from "../components/EditVehicleForm";

vi.mock("../services/vehicleService", () => ({
    getVehicleById: vi.fn().mockResolvedValue({
        id: "1",
        make: "Toyota",
        model: "Corolla",
        category: "Sedan",
        price: 22000,
        quantity: 5,
    }),
    updateVehicle: vi.fn(),
}));

test("renders edit vehicle form", async () => {

    render(

        <MemoryRouter initialEntries={["/vehicles/edit/1"]}>

            <Routes>

                <Route
                    path="/vehicles/edit/:id"
                    element={<EditVehicleForm />}
                />

            </Routes>

        </MemoryRouter>

    );

    expect(await screen.findByDisplayValue("Toyota"))
        .toBeInTheDocument();

});