# pt-BR locale — Phase 1 progress report

Date: 2026-10-13

## Scope

Followed the localization roadmap order for the Brazilian Portuguese pilot:

1. Created the `pt-BR` locale scaffold.
2. Added draft Level 1 translations in the locale pack.
3. Added Brazilian-Portuguese learner notes (`l1_notes.json`) for false friends,
   pronunciation transfer, and grammar transfer.
4. Exported Level 1 review batches for native review.

All new locale entries remain `draft`. No entry was marked `reviewed` or `approved`.
The locale must not be selectable or bundled for release until a native reviewer imports
the reviewed CSV and the owner approves it.

## Current status (after the manual word, explanation, and gloss-quality pass)

| Kind | Draft | Missing | Total |
|---|---:|---:|---:|
| Word senses | 8,837 | 0 | 8,837 |
| Topics | 47 | 0 | 47 |
| Confusables | 32 | 0 | 32 |
| Grammar | 44 | 0 | 44 |
| Question explanations | 890 | 0 | 890 |
| Passage explanations | 40 | 0 | 40 |
| UI strings | 728 | 0 | 728 |

`l1_notes.json` contains 10 false-friend notes, 6 pronunciation notes, and 8 grammar
notes written for Brazilian Portuguese learners. All 8,837 word senses, 47 topics, 32
confusables, 44 grammar points, 890 question explanations, 40 passage explanations,
and 728 UI keys now have Brazilian-Portuguese drafts. The word-sense level totals are
Level 1: 1,277, Level 2: 1,621, Level 3: 2,100, Level 4: 3,545, and Level 5: 131;
all are complete in draft status.

The gloss-quality pass reviewed the delimiter convention used by the draft pack. Text
after `;` was explanatory nuance rather than an additional learner equivalent, so the
compact gloss keeps the primary equivalent. Function-word variants and three
context-sensitive long entries were handled with explicit concise forms. Result: zero
pt-BR glosses over 40 characters and zero with more than three comma-separated
equivalents.
  with placeholders preserved. English words, examples, IDs, and placeholders remain
  unchanged where they are teaching content.

## Manual content check

The completed batches were checked for natural Brazilian Portuguese, preservation of the
English sense, sentence punctuation, and stable IDs. The following source anomalies were
left visible for reviewer attention rather than silently changing the English source:

- `medicine_s1` has a definition/example mismatch in the English source.
- `headache_s1` has an unusual source definition.
- `fan_s1`/`fan_s2` have swapped-looking source definitions/examples.
- `lose_s2` has a competition-oriented definition but a lost-keys example.
- `cherry_s1`, `octopus_s1`, `shrimp_s1`, `donkey_s1`, `duckling_s1`, `ruler_s1` and
  `scooter_s1` also have definition/example conflicts in the English source; the draft
  follows the learner-facing example and leaves the source unchanged.
- Level 2 batches 027–076 and Level 3 batches 077–129 were manually checked for Brazilian usage, agreement,
  punctuation, and context-specific senses such as `charger_s1`, `battery_s1`, and
  `its_s1`, `adverb_s1`, `antonym_s1`, `auxiliary_s1`, `bureau_s1`, `marble_s1`,
  `freshman_s1`, `harness_s1`, `pronoun_s1`, `underneath_s1`, `programme_s1`,
  `face_to_face_s1`, `hiphop_s1`, and `out_of_date_s1`; Level 3 included separate
  checks for employment and finance senses such as `benefit_s1`/`benefit_s2`,
  `promote_s1`/`promote_s2`, `leave_s1`/`leave_s2`, and `loan_s1`/`loan_s2`.
  `reference_s2` follows the learner-facing “future reference” example because the
  English source definition and example are inconsistent; the English source was not changed.
  Level 3 batch 078 separately checked financial senses such as `interest_s2`/`interest_s3`,
  `balance_s1`/`balance_s2`, `transfer_s1`/`transfer_s2`, `benefit_s1`/`benefit_s2`,
  and `estimate_s1`/`estimate_s2`.
  Batch 079 also checked marketing pairs such as `target_s1`/`target_s2`,
  `survey_s1`/`survey_s2`, `launch_s1`/`launch_s2`, and `commercial_s1`/`commercial_s2`.
  `figure_s3`, `charge_s2`, and `charge_s3` follow their learner-facing examples because
  the English source definitions and examples are inconsistent; the English source was not changed.
  Batch 080 similarly recorded source/example conflicts for `complaint_s1`, `bargain_s1`,
  `checkout_s1`, `court_s2`, and `draft_s1`/`draft_s2`.
  Batch 081 retained learner-facing meanings for the source/example conflict in `track_s2`
  and the source definition conflict in `goods_s1`.
  Batch 082 checked travel and event vocabulary, including the source/example conflicts in
  `arrival_s1`, `reservation_s1`, `fare_s1`/`fare_s2`, and `host_s1`/`host_s2`/`host_s3`.
  Batch 083 checked technology/health vocabulary and retained learner-facing examples for
  source mismatches such as `network_s3`, `backup_s1`, and `attachment_s1`.
  Batch 084 checked housing/education vocabulary and retained the example-led meanings
  for source conflicts such as `degree_s1`/`degree_s2`, `graduate_s1`/`graduate_s2`,
  and `fluent_s1`.
  Batch 085 checked environment/news vocabulary, preserving the source distinctions for
  `press_s1`/`press_s2` and `issue_s1`/`issue_s2`.
  Batch 086 checked academic/family vocabulary and retained example-led meanings for
  `raise_s2` and `crush_s1`, whose English source definitions conflict with their examples.
  Batch 087 checked food, weather, and sports vocabulary; `disappointed_s1` and
  `bitter_s1` were aligned to the learner-facing examples while preserving the source.
  Batch 088 checked entertainment and body vocabulary, including the example-led
  meanings for `plot_s1`, `spoiler_s1`, and `catchy_s1`.
  Batch 089 checked animals, clothing, and digital-life vocabulary; `block_s1` and
  `notification_s1` were aligned to their learner-facing examples where the source definition differs.
  Batch 090 checked informal digital expressions and phrasal verbs, including the distinct
  senses of `chill_s1`/`chill_s2` and the example-driven `chill_s1` source conflict.
  Batch 091 checked idioms and linking words, including the learner-facing distinction
  between `especially_s1` and `specially_s1`.
  Batch 092 checked common academic/work verbs and retained the distinct uses of
  `rank_s1`, `grade_s1`, `attach_s1`, and `retain_s1`.
  Batch 093 checked general-purpose verbs and units, including `inch_s1`, `bid_s1`,
  `breed_s1`, and `mount_s1`.
  Batch 094 checked motion, work, and description verbs such as `adapt_s1`, `undertake_s1`,
  `yield_s1`, `regulate_s1`, and `secure_s1`.
  Batch 095 checked descriptive and adverb vocabulary, preserving distinctions such as
  `attractive_s1`, `somewhat_s1`, and `closely_s1`.
  Batch 096 checked descriptive, color, direction, and academic vocabulary, including
  the context-sensitive phrasal adverb `forth_s1` and the adjective senses of
  `valuable_s1`, `capable_s1`, `consistent_s1`, and `thick_s1`.
  Batch 097 checked materials, likelihood, descriptive adjectives, linking adverbs,
  and context-sensitive senses such as `spare_s1`, `reverse_s1`, `secondary_s1`, and
  `badly_s1`.
  Batch 098 checked academic, descriptive, and social vocabulary, including the
  context-sensitive senses of `crazy_s1`, `mad_s1`, `keen_s1`, and `remote_s1`.
  Batch 099 checked people, materials, weather, and descriptive vocabulary, preserving
  context-specific choices such as `voluntary_s1` as “opcional”, `royal_s1` as “real”,
  and `tender_s1` as the food sense “macio”.
  Batch 100 checked abstract nouns and everyday actions, including the example-led
  choices for `row_s1`, `native_s1`, `employment_s1`, `provision_s1`, and `lock_s1`.
  Batch 101 checked places, society, quantities, and actions, including Brazilian usage
  choices such as `yard_s1` as “quintal”, `dozen_s1` as “dúzia”, and `knock_s1` as
  “bater” at a door.
  Batch 102 checked academic and abstract vocabulary, including the example-led senses
  of `passage_s1`, `examination_s1`, `prospect_s1`, and `comfort_s1`.
  Batch 103 checked nature, public life, and everyday actions, with example-led choices
  such as `proof_s1` as “comprovante”, `pitch_s1` as “campo”, and `crop_s1` as “cultivo”.
  Batch 104 checked geography, technology, public life, and everyday nouns, preserving
  `demonstration_s1`, `mode_s1`, `vice_s1`, and `membership_s1` in their contextual senses.
  Batch 105 checked social, health, academic, and household vocabulary, including the
  context-sensitive choices `gender_s1`, `stroke_s1`, `compound_s1`, and `slave_s1`.
  Batch 106 checked government, commerce, nature, and household vocabulary, including
  `compromise_s1`, `proof_s1`-style document contexts, `scope_s1`, and `rear_s1`.
  Batch 107 checked law, travel, science, grammar, and safety vocabulary, preserving
  `sanction_s1`, `attraction_s1`, `lover_s1`, `verb_s1`, and `grain_s1` by context.
  Batch 108 checked probability, health, body, position, and safety vocabulary, including
  `ownership_s1`, `registration_s1`, `dispose_s1`, `compensate_s1`, and `recovery_s1`.
  Batch 109 checked health, safety, education, and everyday action vocabulary, including
  `depression_s1`, `abide_s1`, `periodical_s1`, and `tissue_s1` in their examples.
  Batch 110 checked business, contracts, devices, directions, and safety vocabulary,
  including the fixed legal phrase `null_s1`, and the contextual senses of `dock_s1`,
  `applicable_s1`, and `constrain_s1`.
  Batch 111 checked academic, digital, employment, and everyday action vocabulary,
  including `derivative_s1`, `cite_s1`, `dismiss_s1`, and `differentiate_s1`.
  Batch 112 checked government, communication, health, and academic vocabulary, including
  the contextual meanings of `bloody_s1`, `accrue_s1`, `entail_s1`, and `remedy_s1`.
  Batch 113 checked work, travel, education, and everyday vocabulary, preserving
  `rid_s1` as “livrar-se de”, `respectively_s1`, and `gate_s1` as the airport context.
  Batch 114 checked technology, society, education, and health vocabulary, including
  `input_s1`, `pupil_s1`, `underlie_s1`, `database_s1`, and `rarely_s1`.
  Batch 115 checked education, health, culture, and general vocabulary, preserving
  `everyday_s1`, `disorder_s1`, `tank_s1`, `intervention_s1`, and `subsequent_s1`.
  Batch 116 checked health, transport, education, and workplace vocabulary, including
  `infection_s1`, `iron_s1`, `ally_s1`, `entitle_s1`, and `infant_s1`.
  Batch 117 checked health, law, society, sports, and language-learning vocabulary,
  including `offense_s1`, `clinical_s1`, `dialog_s1`, `capability_s1`, and `barrier_s1`.
  Batch 118 checked education, culture, work, science, and everyday vocabulary, including
  `counter_s1`, `journal_s1`, `fulfill_s1`, `publisher_s1`, and `celebration_s1`.
  Batch 119 checked finance, health, education, culture, and abstract vocabulary, including
  `versus_s1`, `therapy_s1`, `gear_s1`, `participation_s1`, and `evolve_s1`.
  Batch 120 checked law, government, culture, travel, and everyday vocabulary, including
  `abortion_s1`, `sanction_s1`-style legal context, `whilst_s1`, `undergo_s1`, and `airline_s1`.
  Batch 121 checked society, education, travel, technology, and workplace vocabulary,
  including `racial_s1`, `narrative_s1`, `pro_s1`, `implementation_s1`, and `guideline_s1`.
  Batch 122 checked travel, public places, emotions, study, and outdoor vocabulary,
  including `reflection_s1`, `transportation_s1`, `substitute_s1`, `summarize_s1`, and `shore_s1`.
  Batch 123 checked work, education, government, technology, and family vocabulary,
  including `comprise_s1`, `attendance_s1`, `qualification_s1`, `server_s1`, and `noun_s1`.
  Batch 124 checked home, travel, commerce, hospitality, and technology vocabulary,
  including `found_s1`, `vacuum_s1`, `voucher_s1`, `tracking_s1`, and `router_s1`.
  Batch 125 checked communication, family, hospitality, office, and technology vocabulary,
  including `get_along_s1`, `turn_up_s1`, `graduation_s1`, `subscription_s1`, and `upgrade_s1`.
  Batch 126 checked home, office, travel, media, and technology vocabulary, including
  `appliance_s1`, `admission_s1`, `inspection_s1`, `overtime_s1`, and `shuttle_s1`.
  Batch 127 checked office, education, media, travel, and workplace vocabulary, including
  `paperwork_s1`, `trainee_s1`, `dental_s1`, `paycheck_s1`, and `internship_s1`.
  Batch 128 checked business, technology, travel, media, and finance vocabulary, including
  `outlet_s1`, `efficiently_s1`, `overview_s1`, `pharmacist_s1`, and `inexpensive_s1`.
  Batch 129 checked travel, office, home, technology, and commerce vocabulary, including
  `airfare_s1`, `faulty_s1`, `misplace_s1`, `programmer_s1`, and `accordingly_s1`.
  Batch 130 checked business, hospitality, administration, and workplace vocabulary,
  including `excessive_s1`, `concierge_s1`, `documentation_s1`, `allocate_s1`, and
  `introductory_s1`.
  Batch 131 checked commerce, travel, public services, education, and workplace vocabulary,
  including `vend_s1`, `authentic_s1`, `healthcare_s1`, `paraphrase_s1`, and `conform_s1`.
  Batch 132 checked commerce, science, education, public services, and workplace vocabulary,
  including `distributor_s1`, `guidance_s1`, `physics_s1`, `rebate_s1`, and `blueprint_s1`.
  Batch 133 checked safety, education, business, housing, and infrastructure vocabulary,
  including `cautious_s1`, `counselor_s1`, `overcharge_s1`, `reimbursement_s1`, and
  `termination_s1`.
  Batch 134 checked commerce, finance, public services, work, and travel vocabulary,
  including `theft_s1`, `transmission_s1`, `waive_s1`, `loyalty_s1`, and `worthwhile_s1`.
  Batch 135 checked safety, weather, education, health, and public-services vocabulary,
  including `advisable_s1`, `contraction_s1`, `evacuate_s1`, `prescribe_s1`, and `retrieve_s1`.
  Batch 136 checked arts, education, technology, public safety, and administration vocabulary,
  including `audition_s1`, `copyright_s1`, `diagnostic_s1`, `outage_s1`, and `reconsider_s1`.
  Batch 137 checked technology, health, education, public administration, and workplace vocabulary,
  including `redesign_s1`, `satellite_s1`, `automotive_s1`, `by_law_s1`, and `nutrition_s1`.
  Batch 138 checked health, safety, science, accessibility, and workplace vocabulary,
  including `wellness_s1`, `ethics_s1`, `deteriorate_s1`, `disability_s1`, and `discourage_s1`.
  Batch 139 checked science, society, health, environment, and relationships vocabulary,
  including `dissolve_s1`, `genetics_s1`, `ecosystem_s1`, `encouragement_s1`, and `drunk_s1`.
  Batch 140 checked history, science, arts, grammar, and education vocabulary,
  including `dynasty_s1`, `extinct_s1`, `infinitive_s1`, `innovator_s1`, and `intermediate_s1`.
  Batch 141 checked history, law, events, accessibility, and education vocabulary,
  including `interval_s1`, `lawful_s1`, `liberate_s1`, `intermission_s1`, and `mathematician_s1`.
  Batch 142 checked education, grammar, health, society, and safety vocabulary,
  including `meditate_s1`, `modal_s1`, `oppression_s1`, `overwhelm_s1`, and `persuasive_s1`.
  Batch 143 checked religion, environment, law, education, and public services vocabulary,
  including `phantom_s1`, `pollutant_s1`, `prohibition_s1`, `purify_s1`, and `rebuild_s1`.
  Batch 144 checked travel, society, safety, events, and sports vocabulary, including
  `regain_s1`, `runway_s1`, `self_service_s1`, `sportsmanship_s1`, and `stunning_s1`.
  Batch 145 checked style, science, health, education, and social vocabulary, including
  `stylish_s1`, `submarine_s1`, `supportive_s1`, `takeoff_s1`, and `transformation_s1`.
  Batch 146 checked safety, travel, technology, health, and workplace vocabulary, including
  `uncontrollable_s1`, `turbulence_s1`, `virtual_s1`, `suicide_s1`, and `analyse_s1`.
  Batch 147 checked grammar, history, law, science, and travel vocabulary, including
  `whom_s1`, `civilisation_s1`, `counselling_s1`, `immigrate_s1`, and `mountaintop_s1`.
  Batch 148 completed the remaining Level 3 vocabulary, checking nature, society, grammar,
  health, geography, and culture entries including `mourn_s1`, `aborigine_s1`, `hiv_s1`,
  `paralyse_s1`, and `specialise_s1`.
  Batch 149 started Level 4 with workplace and business vocabulary including `extension_s1`,
  `delegate_s1`, `probation_s1`, `revenue_s1`, `quote_s1`, and `retail_s1`. The English
  `extension_s1` definition/example are inconsistent (deadline extension versus phone
  extension); the draft preserves both learner contexts for native review.
  Batch 150 checked commerce, manufacturing, logistics, finance, and workplace vocabulary,
  including `wholesale_s1`, `negotiate_s1`, `inventory_s1`, `maintenance_s1`, and `bulk_s1`.
  Batch 151 checked travel, hospitality, food, technology, and health vocabulary, including
  `customer_service_s1`, `itinerary_s1`, `check_in_s1`, `artificial_intelligence_s1`, and
  `prescription_s1`. Several English definitions/examples use different senses (`accommodation_s1`,
  `vacant_s1`, `suite_s1`, and `booth_s1`); the drafts preserve the learner-facing example context.
  Batch 152 checked health, housing, education, environment, media, and daily-life vocabulary,
  including `vaccine_s1`, `tenant_s1`, `tuition_s1`, `renewable_s1`, `documentary_s1`, and
  `look_after_s1`.
  Batch 153 checked work, family, food, weather, travel, and exercise vocabulary, including
  `significant_s1`, `approach_s1`, `get_married_s1`, `street_food_s1`, `heatwave_s1`, and
  `work_out_s1`/`work_out_s2`.
  Batch 154 checked entertainment, nature, travel, technology, and conversational vocabulary,
  including `binge_watch_s1`, `masterpiece_s1`, `habitat_s1`, `screenshot_s1`, `my_bad_s1`,
  and `you_got_this_s1`.
  Batch 155 checked conversational phrasal verbs and idioms, including `by_the_way_s1`,
  `pick_up_s1`/`pick_up_s2`, `get_over_s1`, `piece_of_cake_s1`, and `think_outside_the_box_s1`.
  Batch 156 checked emotions, science, work, grammar, and safety vocabulary, including
  `feel_blue_s1`, `economic_s1`/`economical_s1`, `absorb_s1`, `comparative_s1`, and `comply_s1`.
  Batch 157 checked law, science, health, education, and workplace vocabulary, including
  `composite_s1`, `consent_s1`, `constitution_s1`, `hydrogen_s1`, and `interfere_s1`.
  Batch 158 checked daily life, science, finance, law, education, and geography vocabulary,
  including `leisure_s1`, `mechanical_s1`, `monopoly_s1`, `prejudice_s1`, `pronounce_s1`,
  and `republic_s1`.
  Batch 159 checked technology, education, law, science, and business vocabulary, including
  `revolutionary_s1`, `systematic_s1`, `translation_s1`, `treaty_s1`, and `accommodate_s1`.
  Batch 160 checked workplace, safety, health, technology, and daily-life vocabulary, including
  `accordance_s1`, `administer_s1`, `attorney_s1`, `allowance_s1`, `behalf_s1`, and `certainty_s1`.
  Batch 161 checked technology, law, business, education, and workplace vocabulary, including
  `circuit_s1`, `collaboration_s1`, `constitutional_s1`, `convey_s1`, and `defendant_s1`.
  Batch 162 checked government, events, safety, education, and workplace vocabulary, including
  `diplomatic_s1`, `dispatch_s1`, `engagement_s1`, `hazard_s1`, and `legacy_s1`.
  Batch 163 checked government, grammar, environment, finance, and safety vocabulary, including
  `legislative_s1`, `mitigate_s1`, `outbreak_s1`, `passive_s1`, and `pledge_s1`.
  Batch 164 checked finance, business, travel, safety, and daily-life vocabulary, including
  `profitable_s1`, `proprietary_s1`, `reconcile_s1`, `safeguard_s1`, and `scratch_s1`.
  Batch 165 checked government, nature, safety, law, and education vocabulary, including
  `secular_s1`, `seize_s1`, `parliament_s1`, `testimony_s1`, and `torture_s1`.
  Batch 166 checked business, travel, health, education, and daily-life vocabulary, including
  `transparent_s1`, `unprecedented_s1`, `authority_s1`, `identification_s1`, and `cafe_s1`.
  Batch 167 checked food, clothing, events, music, travel, and household vocabulary, including
  `bean_s1`, `parade_s1`, `conductor_s1`, `repetition_s1`, and `turnover_s1`.
  Batch 168 checked food, travel, health, legal, and workplace vocabulary, including
  `cooler_s1`, `discharge_s1`, `compliance_s1`, `subway_s1`, and `chancellor_s1`.
  Batch 169 checked science, law, government, nature, and everyday vocabulary, including
  `spark_s1`, `nucleus_s1`, `warrant_s1`, `compass_s1`, and `colony_s1`.
  Batch 170 checked law, society, nature, science, and everyday vocabulary, including
  `rebel_s1`, `tribunal_s1`, `cock_s1`, `conscience_s1`, and `draught_s1`.
  Batch 171 checked health, nature, law, education, and everyday vocabulary, including
  `sweat_s1`, `wilderness_s1`, `abacus_s1`, `hostage_s1`, and `meditation_s1`.
  Batch 172 checked writing, nature, food, travel, and household vocabulary, including
  `relish_s1`, `warning_s1`, `acquaintance_s1`, `hammer_s1`, and `wit_s1`.
  Batch 173 checked travel, nature, science, society, and household vocabulary, including
  `anchor_s1`, `nod_s1`, `backbone_s1`, `coconut_s1`, and `marine_s1`.
  Batch 174 checked food, family, travel, education, and society vocabulary, including
  `quarrel_s1`, `understanding_s1`, `bachelor_s1`, `breadth_s1`, and `kingdom_s1`.
  Batch 175 checked travel, law, food, education, and household vocabulary, including
  `obedience_s1`, `prince_s1`, `seizure_s1`, `filling_s1`, and `mast_s1`.
  Batch 176 checked daily life, society, food, law, and technology vocabulary, including
  `means_s1`, `proverb_s1`, `robbery_s1`, `socket_s1`, and `editing_s1`.
  Batch 177 checked travel, health, weather, food, and household vocabulary, including
  `fortnight_s1`, `hearing_s1`, `platypus_s1`, `snowfall_s1`, and `mushroom_s1`.
  Batch 178 checked education, technology, law, travel, and workplace vocabulary, including
  `spelling_s1`, `verification_s1`, `avalanche_s1`, `companion_s1`, and `humanity_s1`.
  Batch 179 checked writing, travel, society, education, and household vocabulary, including
  `prose_s1`, `refuge_s1`, `academy_s1`, `courage_s1`, and `foil_s1`.
  Batch 180 checked food, travel, grammar, science, and daily-life vocabulary, including
  `housing_s1`, `kindness_s1`, `navigation_s1`, `prism_s1`, and `stew_s1`.
  Batch 181 checked health, society, household, education, and travel vocabulary, including
  `sting_s1`, `third_s1`, `ambulance_s1`, `couch_s1`, and `dart_s1`.
  Batch 182 checked weather, education, health, food, and society vocabulary, including
  `dew_s1`, `drawing_s1`, `abundance_s1`, `quack_s1`, and `recompense_s1`.
  Batch 183 checked government, law, travel, science, and emergency vocabulary, including
  `riser_s1`, `siege_s1`, `advanced_s1`, `artillery_s1`, and `charcoal_s1`.
  Batch 184 checked business, weather, engineering, travel, and household vocabulary, including
  `complexion_s1`, `conformity_s1`, `engineering_s1`, `funeral_s1`, and `huddle_s1`.
  Batch 185 checked language, religion, society, travel, and daily-life vocabulary, including
  `idiom_s1`, `mastery_s1`, `monarchy_s1`, `nationality_s1`, and `repository_s1`.
  Batch 186 checked religion, nature, travel, health, and household vocabulary, including
  `robe_s1`, `salvation_s1`, `shark_s1`, `twilight_s1`, and `vocation_s1`.
  Batch 187 checked work, society, law, nature, and music vocabulary, including
  `aftermath_s1`, `alibi_s1`, `bulkhead_s1`, `epitome_s1`, and `falsetto_s1`.
  Batch 188 checked nature, travel, engineering, food, and daily-life vocabulary, including
  `fishing_s1`, `footage_s1`, `guardian_s1`, `hardness_s1`, and `mend_s1`.
  Batch 189 checked health, society, education, language, and daily-life vocabulary, including
  `moderation_s1`, `mythology_s1`, `precedence_s1`, `reasoning_s1`, and `speaking_s1`.
  Batch 190 checked society, travel, safety, finance, and daily-life vocabulary, including
  `stature_s1`, `suspense_s1`, `usury_s1`, `camping_s1`, and `hollow_s1`.
  Batch 191 checked measurement, food, technology, education, and science vocabulary, including
  `litre_s1`, `pancake_s1`, `shopping_s1`, `astronomy_s1`, and `sore_s1`.
  Batch 192 checked health, education, language, society, and nature vocabulary, including
  `swallow_s1`, `deaf_s1`, `warfare_s1`, `compassion_s1`, and `volcano_s1`.
  Batch 193 checked society, education, language, science, and nature vocabulary, including
  `bloc_s1`, `despair_s1`, `easter_s1`, `frustum_s1`, and `participle_s1`.
  Batch 194 checked culture, travel, law, health, and daily-life vocabulary, including
  `pique_s1`, `reef_s1`, `anonymity_s1`, `brainchild_s1`, and `carelessness_s1`.
  Batch 195 checked food, technology, history, science, and society vocabulary, including
  `china_s1`, `collusion_s1`, `gratitude_s1`, `gravitation_s1`, and `midday_s1`.
  Batch 196 checked travel, culture, society, science, and daily-life vocabulary, including
  `peacock_s1`, `pretext_s1`, `reciprocal_s1`, `rivalry_s1`, and `telegram_s1`.
  Batch 197 checked history, health, society, science, and education vocabulary, including
  `thud_s1`, `voyage_s1`, `whereabouts_s1`, `astronomical_s1`, and `our_s1`.
  Batch 198 checked grammar, education, health, technology, and society vocabulary, including
  `transitive_s1`, `betray_s1`, `canonical_s1`, `applaud_s1`, and `circular_s1`.
  Batch 199 checked food, nature, engineering, weather, and society vocabulary, including
  `crisp_s1`, `glaring_s1`, `propitious_s1`, `worm_s1`, and `fishy_s1`.
  Batch 200 checked food, travel, science, health, and language vocabulary, including
  `hearty_s1`, `illuminate_s1`, `sacred_s1`, `disperse_s1`, and `grate_s1`.
  Batch 201 checked grammar, society, health, travel, and daily-life vocabulary, including
  `hug_s1`, `imperial_s1`, `obstruct_s1`, `stubborn_s1`, and `accredit_s1`.
  Batch 202 checked society, language, health, science, and daily-life vocabulary, including
  `agog_s1`, `conscientious_s1`, `cultivate_s1`, `imitate_s1`, and `orderly_s1`.
  Batch 203 checked work, travel, nature, language, and sports vocabulary, including
  `picket_s1`, `spiritual_s1`, `thoughtful_s1`, `amphibious_s1`, and `backhanded_s1`.
  Batch 204 checked society, travel, work, health, and daily-life vocabulary, including
  `barren_s1`, `cement_s1`, `detain_s1`, `gracious_s1`, and `hopeless_s1`.
  Batch 205 checked weather, law, education, grammar, and daily-life vocabulary, including
  `hustle_s1`, `impending_s1`, `interested_s1`, `navigate_s1`, and `reverend_s1`.
  Batch 206 checked anatomy, law, government, travel, education, and daily-life vocabulary,
  including `rib_s1`, `salient_s1`, `sanguine_s1`, `winnow_s1`, and `adjective_s1`.
  Batch 207 checked education, grammar, society, language, and daily-life vocabulary,
  including `assimilate_s1`, `attentive_s1`, `cherish_s1`, `interrogative_s1`, and `feminine_s1`.
  Batch 208 checked numbers, weather, grammar, language, and daily-life vocabulary, including
  `fifty_s1`, `forbear_s1`, `gorgeous_s1`, `imaginary_s1`, and `interesting_s1`.
  Batch 209 checked education, travel, law, finance, and daily-life vocabulary, including
  `learned_s1`, `maritime_s1`, `miserable_s1`, `registered_s1`, and `risible_s1`.
  Batch 210 checked travel, nature, education, language, and daily-life vocabulary, including
  `roam_s1`, `sidereal_s1`, `sorrow_s1`, `superlative_s1`, and `vanquish_s1`.
  Batch 211 checked science, weather, grammar, society, and daily-life vocabulary, including
  `vastly_s1`, `volcanic_s1`, `ambiguous_s1`, `beneficent_s1`, and `discriminating_s1`.
  Batch 212 checked health, safety, language, grammar, and daily-life vocabulary, including
  `dreadful_s1`, `malignant_s1`, `forgive_s1`, `healing_s1`, and `incongruous_s1`.
  Batch 213 checked work, finance, education, language, and daily-life vocabulary, including
  `insinuate_s1`, `overwhelming_s1`, `preferred_s1`, `readable_s1`, and `shining_s1`.
  Batch 214 checked education, work, health, and everyday vocabulary, including `adolescent_s1`,
  `comprehension_s1`, `inference_s1`, `productivity_s1`, and `questionnaire_s1`.
  Batch 215 checked business, workplace, finance, and administration vocabulary, including
  `carrier_s1`, `expertise_s1`, `occupancy_s1`, `retailer_s1`, and `finalize_s1`.
  Batch 216 checked workplace, education, travel, finance, and housing vocabulary, including
  `accomplishment_s1`, `mandatory_s1`, `accessible_s1`, `productivity_s1`, and `realtor_s1`.
  Batch 217 checked business, education, transport, finance, and workplace vocabulary, including
  `spokesperson_s1`, `mislead_s1`, `statistics_s1`, `certification_s1`, and `informative_s1`.
  Batch 218 checked workplace, education, medicine, law, and grammar vocabulary, including
  `mentor_s1`, `physician_s1`, `recruitment_s1`, `intonation_s1`, and `reflexive_s1`.
  Batch 219 checked economics, society, health, education, and daily-life vocabulary, including
  `recession_s1`, `accessibility_s1`, `adoption_s1`, `adore_s1`, and `adversity_s1`.
  Batch 220 checked health, technology, media, science, and daily-life vocabulary, including
  `alcoholism_s1`, `alliance_s1`, `amplifier_s1`, `antibacterial_s1`, and `antivirus_s1`.
  Batch 221 checked education, science, nature, society, and daily-life vocabulary, including
  `archaeologist_s1`, `aristocracy_s1`, `aspiration_s1`, `atypical_s1`, and `balanced_s1`.
  Batch 222 checked finance, contracts, business, technology, and sustainability vocabulary,
  including `reimburse_s1`, `balance_sheet_s1`, `comply_with_s1`, `troubleshoot_s1`, and
  `sustainable_s1`.
  Batch 223 checked environment, science, grammar, idioms, and society vocabulary, including
  `carbon_footprint_s1`, `hypothesis_s1`, `algorithm_s1`, `burn_the_midnight_oil_s1`, and
  `civilization_s1`.
  Batch 224 checked science, health, grammar, nature, and daily-life vocabulary, including
  `consciousness_s1`, `correspondence_s1`, `gravity_s1`, `kidney_s1`, and `plural_s1`.
  Batch 225 checked science, grammar, society, history, and daily-life vocabulary, including
  `pragmatic_s1`, `reservoir_s1`, `scripture_s1`, `slavery_s1`, and `triangle_s1`.
  Batch 226 checked science, business, finance, arts, and daily-life vocabulary, including
  `velocity_s1`, `dividend_s1`, `appraisal_s1`, `pharmaceutical_s1`, and `benevolence_s1`.
  Batch 227 checked science, technology, society, health, and daily-life vocabulary, including
  `biotechnology_s1`, `bloodstream_s1`, `botany_s1`, `breakdown_s1`, and `brewery_s1`.
  Batch 228 checked media, business, safety, culture, and daily-life vocabulary, including
  `broadband_s1`, `bureaucracy_s1`, `burglary_s1`, `caption_s1`, and `carpool_s1`.
  Batch 229 checked science, medicine, arts, public services, and daily-life vocabulary,
  including `catastrophe_s1`, `chemotherapy_s1`, `circuitry_s1`, `classification_s1`, and
  `coalition_s1`.
  Batch 230 checked geography, business, science, grammar, and communication vocabulary,
  including `coastline_s1`, `coherence_s1`, `collocation_s1`, `compression_s1`, and
  `conjunction_s1`.
  Batch 231 checked history, government, science, arts, and daily-life vocabulary, including
  `conquest_s1`, `consensus_s1`, `corruption_s1`, `craftsman_s1`, and `creepy_s1`.
  Batch 232 checked language, technology, health, nature, and daily-life vocabulary, including
  `crystallize_s1`, `cybercrime_s1`, `decomposition_s1`, `deduce_s1`, and `deem_s1`.
  Batch 233 checked environment, society, grammar, health, and daily-life vocabulary, including
  `deforestation_s1`, `deliberate_s1`, `determiner_s1`, `dictator_s1`, and `discriminate_s1`.
  Batch 234 checked health, law, diplomacy, society, and daily-life vocabulary, including
  `bulimia_s1`, `diphtheria_s1`, `diplomat_s1`, `displace_s1`, and `distortion_s1`.
  Batch 235 checked education, finance, environment, science, and daily-life vocabulary, including
  `diversion_s1`, `earnings_s1`, `ecologist_s1`, `electronics_s1`, and `elegance_s1`.
  Batch 236 checked science, ethics, public safety, arts, and education vocabulary, including
  `elemental_s1`, `eloquence_s1`, `enforce_s1`, `evacuation_s1`, and `examinee_s1`.
  Batch 237 checked education, society, health, transport, and daily-life vocabulary, including
  `exclusion_s1`, `exile_s1`, `fascination_s1`, `fearsome_s1`, and `ferryboat_s1`.
  Batch 238 checked health, technology, education, nature, and daily-life vocabulary, including
  `fiber_s1`, `firearm_s1`, `fluency_s1`, `forestry_s1`, and `fragrance_s1`.
  Batch 239 checked government, society, finance, environment, and daily-life vocabulary, including
  `fraud_s1`, `functionality_s1`, `gambling_s1`, `governmental_s1`, and `gradual_s1`.
  Batch 240 checked society, arts, health, education, and daily-life vocabulary, including
  `graphically_s1`, `gravestone_s1`, `guerrilla_s1`, `hatching_s1`, and `helicopter_s1`.
  Batch 241 checked household, society, health, technology, and daily-life vocabulary, including
  `heritage_s1`, `hospice_s1`, `housemaster_s1`, `humanize_s1`, and `humiliation_s1`.
  Batch 242 checked writing, art, society, health, and daily-life vocabulary, including
  `hyphen_s1`, `ideology_s1`, `impartial_s1`, `impractical_s1`, and `inborn_s1`.
  The Level 4 source contains several archaic or mismatched example phrases (`cock_s1`,
  `oath_s1`, `grind_s1`, `lick_s1`, `lord_s1`, `teat_s1`, `clew_s1`, `rag_s1`, `bosom_s1`,
  `canopy_s1`, `flame_s1`, `mercy_s1`, `prick_s1`, `skite_s1`, `fatal_s1`, `concord_s1`,
  `bearing_s1`, `clearing_s1`, `seizure_s1`, `trimming_s1`, `berry_s1`, `blade_s1`,
  `cropper_s1`, `dressing_s1`, `murmur_s1`, `shaft_s1`, `spit_s1`, `abeyance_s1`,
  `sounding_s1`, `snatch_s1`, `egress_s1`, `fiend_s1`, `affectation_s1`, `trepidation_s1`,
  `dusting_s1`, `ether_s1`, `hiding_s1`, `stunt_s1`, `winning_s1`, `yarn_s1`, `boiling_s1`,
  `eating_s1`, `flop_s1`, `lather_s1`, `scotch_s1`, `whip_s1`, `ram_s1`, `failing_s1`,
  `touching_s1`, `wanting_s1`, `sat_s1`, `spill_the_tea_s1`, `clink_s1`, `poop_s1`,
  `pope_s1`, `candied_s1`, `lushy_s1`, `slapping_s1`, `whip_s1`, `horny_s1`, `stink_s1`,
  `lour_s1`, `wicked_s1`, `devour_s1`, `dwell_s1`, `whither_s1`, `farce_s1`, and `hanging_s1`.
  Batches 209–213 also contain source examples that are archaic, malformed, or mismatched
  (`locomotive_s1`, `orthogonal_s1`, `plank_s1`, `procession_s1`, `justified_s1`,
  `get_cold_feet_s1`, `guarded_s1`, `included_s1`, `jaded_s1`, and `dated_s1`), plus the
  outdated source wording in `imbecile_s1`; the drafts use clear learner-facing Brazilian
  Portuguese and preserve the English source for native review.
  the drafts use a learner-facing
  Brazilian-Portuguese meaning and preserve the English source unchanged for reviewer attention.

### Explanation batches

Question explanation batches 272–297 were checked manually against each English stem,
answer choice, and grammar point. Passage explanation batches 298–299 were checked
against all four blanks in each passage; the English passage text, options, and answer
indices were not changed. Portuguese explanations retain required English target forms
where the explanation teaches a specific form or collocation.

## Checks

- `python tools/locale.py check --all`: PASS, all locales report `errors: 0`.
- `python tools/validate_content.py`: PASS, `errors=0`, `warnings=64`; the remaining
  warnings are legacy long-gloss heuristics in es/vi. The validator no longer flags a
  concise learner gloss merely because the English WordNet definition is verbose. The
  pt-BR locale contributes zero warnings.
- `python tools/audit_content.py`: completed; the English source semantic review reports
  1,770 errors at the current checkpoint, down from the 2,574 baseline. See
  `dist/reports/source-semantic-review-2026-10-13.md`; this is still not a clean-source gate.
- `./gradlew testDebugUnitTest assembleDebug --no-daemon`: PASS (48 actionable tasks).

The current focused audit reports 1,770 errors. Most are still English source-quality
findings (legacy example fragments, source IPA formatting, and catch-all topics); a manual
source cleanup is in progress and is not complete. The remaining gloss-style findings are
mostly Brazilian Portuguese
capitalization or abbreviation forms for proper names and terms such as `URL`, `CD`,
`Páscoa`, and `Buda`; they are not long-gloss or equivalent-count failures.

## Gate still open

Native Brazilian Portuguese review is still required. After review, import the returned
CSV with `tools/locale.py import`, then run the approval gate and re-run the full checks.
This report does not claim the locale is release-ready.
