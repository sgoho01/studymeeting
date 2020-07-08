package com.ghsong.studymeeting;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

@AnalyzeClasses(packagesOf = App.class)
public class PackageDependencyTests {

    public static final String STUDY = "..modules.study..";
    public static final String EVENT = "..modules.event..";
    public static final String ACCOUNT = "..modules.account..";
    public static final String TAG = "..modules.tag..";
    public static final String ZONE = "..modules.zone..";


    @ArchTest
    ArchRule modulesPackageRule = classes().that().resideInAPackage("com.studymeeting.modules..")
            .should().onlyBeAccessed().byClassesThat()
            .resideInAnyPackage("com.studymeeting.modules..");

    @ArchTest
    ArchRule studyPackageRule = classes().that().resideInAPackage(STUDY)
            .should().onlyBeAccessed().byClassesThat()
            .resideInAnyPackage(STUDY, EVENT);

    @ArchTest
    ArchRule eventPackageRule = classes().that().resideInAPackage(EVENT)
            .should().accessClassesThat().resideInAnyPackage(EVENT, STUDY, ACCOUNT);

    @ArchTest
    ArchRule accountPackageRule = classes().that().resideInAPackage(ACCOUNT)
            .should().accessClassesThat().resideInAnyPackage(ACCOUNT, TAG, ZONE);

    @ArchTest
    ArchRule cycleCheck = slices().matching("com.studymeeting.(*)..")
            .should().beFreeOfCycles();

}
