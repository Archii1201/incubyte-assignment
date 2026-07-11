import { describe, test, expect, vi, beforeEach } from "vitest";
import { render, screen } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";

import VehicleList from "../pages/VehicleList";

import {
    searchVehicles,
    purchaseVehicle,
} from "../services/vehicleService";

vi.mock("../services/vehicleService", () => ({
    searchVehicles: vi.fn(),
    purchaseVehicle: vi.fn(),
}));

describe("Purchase Vehicle", () => {

    beforeEach(() => {

        vi.clearAllMocks();

        localStorage.setItem("role", "USER");
    });

    test("shows purchase button for available vehicle", async () => {

        searchVehicles.mockResolvedValue({
            content: [
                {
                    id: "1",
                    make: "Toyota",
                    model: "Corolla",
                    price: 22000,
                    quantity: 5,
                },
            ],
            totalPages: 1,
        });

        render(
            <MemoryRouter>
                <VehicleList />
            </MemoryRouter>
        );

        expect(
            await screen.findByText("Purchase")
        ).toBeInTheDocument();
    });

});