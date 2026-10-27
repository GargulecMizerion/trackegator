package org.kayman.trackegator.carrier.inpost;

import org.kayman.trackegator.carrier.spi.ShipmentStatus;

import static org.kayman.trackegator.carrier.spi.ShipmentStatus.*;

class InPostStatusMapper {

    static ShipmentStatus map(String inPostStatus) {
        if (inPostStatus == null) {
            return UNKNOWN;
        }
        return switch (inPostStatus) {
            case "created", "offers_prepared", "offer_selected", "confirmed" -> CREATED;

            case "dispatched_by_sender", "dispatched_by_sender_to_pok", "collected_from_sender",
                 "taken_by_courier", "taken_by_courier_from_pok", "adopted_at_source_branch",
                 "sent_from_source_branch", "adopted_at_sorting_center", "sent_from_sorting_center",
                 "adopted_at_target_branch", "delay_in_delivery", "redirect_to_box",
                 "canceled_redirect_to_box", "readdressed", "stack_in_customer_service_point",
                 "unstack_from_customer_service_point", "stack_in_box_machine",
                 "unstack_from_box_machine" -> IN_TRANSIT;

            case "out_for_delivery", "out_for_delivery_to_address",
                 "pickup_reminder_sent_address" -> OUT_FOR_DELIVERY;

            case "ready_to_pickup", "ready_to_pickup_from_pok", "ready_to_pickup_from_pok_registered",
                 "ready_to_pickup_from_branch", "pickup_reminder_sent",
                 "courier_avizo_in_customer_service_point" -> READY_FOR_PICKUP;

            case "delivered" -> DELIVERED;

            case "returned_to_sender", "return_pickup_confirmation_to_sender",
                 "taken_by_courier_from_customer_service_point" -> RETURNED;

            case "canceled" -> CANCELED;

            case "oversized", "pickup_time_expired", "avizo", "claimed", "rejected_by_receiver",
                 "undelivered", "undelivered_wrong_address", "undelivered_incomplete_address",
                 "undelivered_unknown_receiver", "undelivered_cod_cash_receiver",
                 "undelivered_no_mailbox", "undelivered_not_live_address",
                 "undelivered_lack_of_access_letterbox", "missing",
                 "stack_parcel_pickup_time_expired",
                 "stack_parcel_in_box_machine_pickup_time_expired" -> EXCEPTION;

            default -> UNKNOWN;
        };
    }
}
