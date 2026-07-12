import { describe, test, expect, vi, beforeEach } from "vitest";
import { render, screen } from "@testing-library/react";
import { BrowserRouter } from "react-router-dom";

import VehicleList from "../pages/VehicleList";
import { searchVehicles } from "../services/vehicleService";

vi.mock("../services/vehicleService", () => ({
    searchVehicles: vi.fn(),
    purchaseVehicle: vi.fn(),
    deleteVehicle: vi.fn(),
    restockVehicle: vi.fn(),
}));

describe("Restock Vehicle", () => {

    beforeEach(() => {

        vi.clearAllMocks();

        localStorage.setItem("role", "ADMIN");
    });

    test("shows restock button for admin", async () => {

        searchVehicles.mockResolvedValue({
            content: [
                {
                    id: "1",
                    make: "Toyota",
                    model: "Corolla",
                    category: "Sedan",
                    quantity: 5,
                    price: 22000,
                },
            ],
            totalPages: 1,
        });

        render(
            <BrowserRouter>
                <VehicleList />
            </BrowserRouter>
        );

        expect(
            await screen.findByText("Restock")
        ).toBeInTheDocument();
    });
});