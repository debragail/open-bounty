-- noinspection SqlResolveForFile
ALTER TABLE public.issues
  DROP COLUMN transaction_hash;
ALTER TABLE public.issues
  DROP COLUMN contract_address;
ALTER TABLE public.issues
  DROP COLUMN confirm_hash;
ALTER TABLE public.issues
  ADD address VARCHAR(256);
ALTER TABLE public.issues
  DROP COLUMN comment_id;
ALTER TABLE public.issues
  DROP COLUMN execute_hash;
ALTER TABLE public.issues
  DROP COLUMN payout_hash;
ALTER TABLE public.issues
  DROP COLUMN payout_receipt;