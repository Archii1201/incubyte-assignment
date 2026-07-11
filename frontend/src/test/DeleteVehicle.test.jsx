import { describe, test, expect, vi, beforeEach } from "vitest";
import { render, screen, waitFor, fireEvent } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";

import VehicleList from "../pages/VehicleList";
import {
    searchVehicles,
    deleteVehicle,
} from "../services/vehicleService";

vi.mock("../services/vehicleService", () => ({
    searchVehicles: vi.fn(),
    deleteVehicle: vi.fn(),
}));

describe("Delete Vehicle", () => {

    beforeEach(() => {
        vi.clearAllMocks();
        localStorage.setItem("role", "ADMIN");
    });

    const pageResponse = {
        content: [
            {
                id: "1",
                make: "Toyota",
                model: "Corolla",
                category: "Sedan",
                price: 22000,
            },
        ],
        totalPages: 1,
    };

    test("shows delete button for admin", async () => {

        searchVehicles.mockResolvedValue(pageResponse);

        render(
            <MemoryRouter>
                <VehicleList />
            </MemoryRouter>
        );

        expect(await screen.findByText("Delete"))
            .toBeInTheDocument();
    });

    test("clicking delete opens confirmation", async () => {

        window.confirm = vi.fn(() => false);

        searchVehicles.mockResolvedValue(pageResponse);

        render(
            <MemoryRouter>
                <VehicleList />
            </MemoryRouter>
        );

        fireEvent.click(await screen.findByText("Delete"));

        expect(window.confirm).toHaveBeenCalled();
    });

    test("confirmed delete calls API", async () => {

        window.confirm = vi.fn(() => true);

        searchVehicles.mockResolvedValue(pageResponse);

        deleteVehicle.mockResolvedValue();

        render(
            <MemoryRouter>
                <VehicleList />
            </MemoryRouter>
        );

        fireEvent.click(await screen.findByText("Delete"));

        await waitFor(() => {

            expect(deleteVehicle)
                .toHaveBeenCalledWith("1");

        });

    });

    test("reloads vehicles after delete", async () => {

        window.confirm = vi.fn(() => true);

        searchVehicles.mockResolvedValue(pageResponse);

        deleteVehicle.mockResolvedValue();

        render(
            <MemoryRouter>
                <VehicleList />
            </MemoryRouter>
        );

        fireEvent.click(await screen.findByText("Delete"));

        await waitFor(() => {

            expect(searchVehicles)
                .toHaveBeenCalledTimes(2);

        });

    });

});